package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.CreateSafeZoneUseCase
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.user.domain.usecase.SearchUsersUseCase
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.utils.toPlainRequestBody
import com.jean.cuidemonosaqp.shared.utils.uriToPart
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class CreateSafeZoneViewModel @Inject constructor(
    private val createSafeZoneUseCase: CreateSafeZoneUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CreateSafeZoneVM"
        private const val MAX_SELECTED_USERS = 3
        private const val MAX_SUGGESTIONS = 5
        private const val MAX_DISTANCE_METERS = 100.0
        private val DEFAULT_LOCATION = LatLng(-16.409047, -71.537451) // Arequipa
    }

    private val _state = MutableStateFlow(SafeZoneUiState())
    val state: StateFlow<SafeZoneUiState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    // Flow optimizado para búsqueda de usuarios
    private val searchResults: StateFlow<List<User>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.length >= 2 }
        .flatMapLatest { query ->
            flow {
                when (val result = searchUsersUseCase(query)) {
                    is NetworkResult.Success -> {
                        val filteredUsers = result.data
                            .filterNot { user -> _state.value.selectedUsers.any { it.id == user.id } }
                            .take(MAX_SUGGESTIONS)
                        emit(filteredUsers)
                    }
                    else -> emit(emptyList())
                }
            }.catch { emit(emptyList()) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Observar resultados de búsqueda
        viewModelScope.launch {
            searchResults.collect { users ->
                _state.update { it.copy(userSuggestions = users) }
            }
        }
        loadCurrentUserLocation()
    }

    private fun loadCurrentUserLocation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingUserLocation = true) }

            try {
                val userId = sessionRepository.getUserId()
                if (userId != null) {
                    when (val response = getUserInfoUseCase(userId)) {
                        is NetworkResult.Success -> {
                            val user = response.data
                            val userLocation = LatLng(
                                user.addressLatitude ?: DEFAULT_LOCATION.latitude,
                                user.addressLongitude ?: DEFAULT_LOCATION.longitude
                            )
                            _state.update {
                                it.copy(
                                    currentUserLocation = userLocation,
                                    isLoadingUserLocation = false
                                )
                            }
                        }
                        is NetworkResult.Error -> {
                            _state.update {
                                it.copy(
                                    isLoadingUserLocation = false,
                                    error = "No se pudo obtener tu ubicación: ${response.message}"
                                )
                            }
                        }
                        else -> { /* Loading state handled above */ }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoadingUserLocation = false,
                            error = "No se encontró información de sesión"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingUserLocation = false,
                        error = "Error al cargar tu ubicación: ${e.message}"
                    )
                }
            }
        }
    }

    // User management functions
    fun onUserSearchChange(query: String) {
        _searchQuery.value = query
        _state.update { it.copy(userSearchQuery = query) }
    }

    fun onUserSelected(user: User) {
        val currentSelected = _state.value.selectedUsers
        if (currentSelected.size < MAX_SELECTED_USERS && !currentSelected.any { it.id == user.id }) {
            _state.update {
                it.copy(
                    selectedUsers = it.selectedUsers + user,
                    userSearchQuery = "",
                    userSuggestions = emptyList()
                )
            }
            _searchQuery.value = ""
        }
    }

    fun onUserRemoved(user: User) {
        _state.update { it.copy(selectedUsers = it.selectedUsers.filterNot { u -> u.id == user.id }) }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _state.update { it.copy(userSearchQuery = "", userSuggestions = emptyList()) }
    }

    // Location validation
    fun isLocationWithinRadius(selectedLocation: LatLng): Boolean {
        val userLocation = _state.value.currentUserLocation ?: return false
        return calculateDistance(userLocation, selectedLocation) <= MAX_DISTANCE_METERS
    }

    fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = 6371000.0
        val lat1Rad = Math.toRadians(location1.latitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val deltaLatRad = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLngRad = Math.toRadians(location2.longitude - location1.longitude)

        val a = kotlin.math.sin(deltaLatRad / 2).pow(2) +
                kotlin.math.cos(lat1Rad) * kotlin.math.cos(lat2Rad) * kotlin.math.sin(deltaLngRad / 2).pow(2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

        return earthRadius * c
    }

    // Form validation
    fun canCreateZone(): Boolean {
        val currentState = _state.value
        return with(currentState) {
            name.isNotBlank() &&
                    justification.isNotBlank() &&
                    latitude.isNotBlank() &&
                    longitude.isNotBlank() &&
                    //statusId.isNotBlank() &&
                    selectedUsers.size >= MAX_SELECTED_USERS &&
                    !isLoading &&
                    !isLoadingUserLocation &&
                    currentUserLocation != null
        }
    }

    // Form field updates
    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onJustificationChange(value: String) = _state.update { it.copy(justification = value) }
    fun onResponsibilityChange(value: Boolean) = _state.update { it.copy(assumesResponsibility = value) }
    fun onLatitudeChange(value: String) = _state.update { it.copy(latitude = value) }
    fun onLongitudeChange(value: String) = _state.update { it.copy(longitude = value) }
    //fun onStatusIdChange(value: String) = _state.update { it.copy(statusId = value) }
    fun onRatingChange(value: String) = _state.update { it.copy(rating = value) }
    fun onCategoryChange(value: String) = _state.update { it.copy(category = value) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }
    fun onImageUriChange(uri: Uri?) = _state.update { it.copy(imageUri = uri) }
    fun onCategoryDropdownToggle() = _state.update {
        it.copy(isCategoryDropdownExpanded = !it.isCategoryDropdownExpanded)
    }

    fun onCategorySelect(category: String) = _state.update {
        it.copy(
            category = category,
            isCategoryDropdownExpanded = false
        )
    }

    fun onCategoryDismiss() = _state.update {
        it.copy(isCategoryDropdownExpanded = false)
    }
    fun submit(contentResolver: ContentResolver) {
        val currentState = _state.value

        if (!canCreateZone()) {
            _state.update {
                it.copy(error = "Debe completar todos los campos requeridos y seleccionar exactamente 3 vigilantes")
            }
            return
        }

        // Validate location is within radius
        if (currentState.latitude.isNotBlank() && currentState.longitude.isNotBlank()) {
            val selectedLocation = LatLng(
                currentState.latitude.toDoubleOrNull() ?: 0.0,
                currentState.longitude.toDoubleOrNull() ?: 0.0
            )
            if (!isLocationWithinRadius(selectedLocation)) {
                _state.update {
                    it.copy(error = "La ubicación seleccionada está fuera del área permitida (${MAX_DISTANCE_METERS.toInt()}m)")
                }
                return
            }
        }

        _state.update { it.copy(isLoading = true, error = null, success = false) }

        viewModelScope.launch {
            try {
                val fields = buildRequestFields(currentState)
                val photoPart = currentState.imageUri?.let { uri ->
                    uriToPart("photo_url", uri, contentResolver)
                }

                val result = createSafeZoneUseCase(fields, photoPart)
                // Asumir que createSafeZoneUseCase retorna el ID de la zona creada
                _state.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        createdSafeZoneId = result.id.toString() // Nuevo campo para almacenar el ID
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al crear zona segura", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error desconocido al crear la zona segura"
                    )
                }
            }
        }
    }

    private fun buildRequestFields(state: SafeZoneUiState): Map<String, RequestBody> {
        return mutableMapOf<String, RequestBody>().apply {
            put("name", state.name.toPlainRequestBody())
            put("justification", state.justification.toPlainRequestBody())
            put("assumes_responsibility", state.assumesResponsibility.toString().toPlainRequestBody())
            put("latitude", state.latitude.toPlainRequestBody())
            put("longitude", state.longitude.toPlainRequestBody())
            val defaultstatus = "4"
            put("status_id", defaultstatus.toPlainRequestBody())
            put("user_ids", state.selectedUsers.joinToString(",") { it.id.toString() }.toPlainRequestBody())
            put("rating", state.rating.ifBlank { "0" }.toPlainRequestBody())

            // Optional fields
            if (state.description.isNotBlank()) {
                put("description", state.description.toPlainRequestBody())
            }
            if (state.category.isNotBlank()) {
                put("category", state.category.toPlainRequestBody())
            }
        }
    }
}

data class SafeZoneUiState(
    val userSearchQuery: String = "",
    val userSuggestions: List<User> = emptyList(),
    val selectedUsers: List<User> = emptyList(),
    val name: String = "",
    val justification: String = "",
    val assumesResponsibility: Boolean = false,
    val latitude: String = "",
    val longitude: String = "",
    //val statusId: String = "",
    val rating: String = "",
    val category: String = "",
    val description: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val currentUserLocation: LatLng? = null,
    val isLoadingUserLocation: Boolean = false,
    val availableCategories: List<String> = listOf(
        "Plaza",
        "Parque",
        "Iglesia",
        "Estación de Bomberos",
        "Comisaría",
        // Categorías más comunes
        "Tienda",
        "Supermercado",
        "Otro"
    ),
    val isCategoryDropdownExpanded: Boolean = false,
    val createdSafeZoneId: String? = null
)