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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
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
    }

    private val _state = MutableStateFlow(SafeZoneUiState())
    val state: StateFlow<SafeZoneUiState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    // Flow para búsqueda de usuarios
    private val searchResults: StateFlow<List<User>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.length >= 2 }
        .onEach { q ->
            Log.d(TAG, "Searching users for query: \"$q\"")
        }
        .flatMapLatest { q ->
            flow {
                when (val res = searchUsersUseCase(q)) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "Got ${res.data.size} users for \"$q\"")
                        // Filtrar usuarios ya seleccionados y limitar a 5
                        val filteredUsers = res.data
                            .filterNot { user ->
                                _state.value.selectedUsers.any { selected -> selected.id == user.id }
                            }
                            .take(MAX_SUGGESTIONS)
                        emit(filteredUsers)
                    }
                    is NetworkResult.Error -> {
                        Log.d(TAG, "Search failed for \"$q\": ${res.message}")
                        emit(emptyList())
                    }
                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading search for \"$q\"")
                        emit(emptyList())
                    }
                }
            }
        }
        .catch { e ->
            Log.e(TAG, "Error in searchResults flow", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            searchResults.collect { list ->
                Log.d(TAG, "Collected suggestions: ${list.size} users")
                _state.update { it.copy(userSuggestions = list) }
            }
        }

        // Cargar información del usuario actual
        loadCurrentUserLocation()
    }

    private fun loadCurrentUserLocation() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoadingUserLocation = true) }

                val currentUserId = sessionRepository.getUserId()
                if (currentUserId != null) {
                    when (val response = getUserInfoUseCase(currentUserId)) {
                        is NetworkResult.Success -> {
                            val user = response.data
                            val userLocation = LatLng(
                                user.addressLatitude ?: -16.409047, // Arequipa por defecto
                                user.addressLongitude ?: -71.537451
                            )
                            _state.update {
                                it.copy(
                                    currentUserLocation = userLocation,
                                    isLoadingUserLocation = false
                                )
                            }
                            Log.d(TAG, "User location loaded: ${userLocation.latitude}, ${userLocation.longitude}")
                        }
                        is NetworkResult.Error -> {
                            Log.e(TAG, "Error loading user location: ${response.message}")
                            _state.update {
                                it.copy(
                                    isLoadingUserLocation = false,
                                    error = "No se pudo obtener tu ubicación: ${response.message}"
                                )
                            }
                        }
                        is NetworkResult.Loading -> {
                            // Mantener estado de carga
                        }
                    }
                } else {
                    Log.e(TAG, "No user ID found in session")
                    _state.update {
                        it.copy(
                            isLoadingUserLocation = false,
                            error = "No se encontró información de sesión"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading user location", e)
                _state.update {
                    it.copy(
                        isLoadingUserLocation = false,
                        error = "Error al cargar tu ubicación: ${e.message}"
                    )
                }
            }
        }
    }

    fun onUserSearchChange(query: String) {
        _searchQuery.value = query
        _state.update { it.copy(userSearchQuery = query) }
    }

    fun onUserSelected(user: User) {
        val currentSelected = _state.value.selectedUsers
        if (currentSelected.size < MAX_SELECTED_USERS &&
            !currentSelected.any { it.id == user.id }) {

            _state.update {
                it.copy(
                    selectedUsers = it.selectedUsers + user,
                    userSearchQuery = "",
                    userSuggestions = emptyList()
                )
            }
            _searchQuery.value = ""
            Log.d(TAG, "User selected: ${user.fullName}. Total selected: ${currentSelected.size + 1}")
        }
    }

    fun onUserRemoved(user: User) {
        _state.update {
            it.copy(selectedUsers = it.selectedUsers.filterNot { u -> u.id == user.id })
        }
        Log.d(TAG, "User removed: ${user.fullName}")
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _state.update { it.copy(userSearchQuery = "", userSuggestions = emptyList()) }
    }

    private fun buildUserIds(): String =
        state.value.selectedUsers.joinToString(",") { it.id.toString() }

    // Validación para habilitar el botón de crear
    fun canCreateZone(): Boolean {
        val state = _state.value
        return state.name.isNotBlank() &&
                state.justification.isNotBlank() &&
                state.latitude.isNotBlank() &&
                state.longitude.isNotBlank() &&
                state.statusId.isNotBlank() &&
                state.selectedUsers.size >= MAX_SELECTED_USERS &&
                !state.isLoading &&
                !state.isLoadingUserLocation &&
                state.currentUserLocation != null
    }

    // Función para verificar si una ubicación está dentro del radio permitido
    fun isLocationWithinRadius(selectedLocation: LatLng): Boolean {
        val userLocation = _state.value.currentUserLocation ?: return false
        return calculateDistance(userLocation, selectedLocation) <= MAX_DISTANCE_METERS
    }

    // Calcular distancia entre dos puntos en metros
    fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = 6371000.0 // Radio de la Tierra en metros

        val lat1Rad = Math.toRadians(location1.latitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val deltaLatRad = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLngRad = Math.toRadians(location2.longitude - location1.longitude)

        val a = kotlin.math.sin(deltaLatRad / 2).pow(2) +
                kotlin.math.cos(lat1Rad) * kotlin.math.cos(lat2Rad) * kotlin.math.sin(deltaLngRad / 2).pow(2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

        return earthRadius * c
    }

    // Resto de funciones onChange...
    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onJustificationChange(value: String) = _state.update { it.copy(justification = value) }
    fun onResponsibilityChange(value: Boolean) = _state.update { it.copy(assumesResponsibility = value) }
    fun onLatitudeChange(value: String) = _state.update { it.copy(latitude = value) }
    fun onLongitudeChange(value: String) = _state.update { it.copy(longitude = value) }
    fun onStatusIdChange(value: String) = _state.update { it.copy(statusId = value) }
    fun onRatingChange(value: String) = _state.update { it.copy(rating = value) }
    fun onCategoryChange(value: String) = _state.update { it.copy(category = value) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }
    fun onImageUriChange(uri: Uri?) = _state.update { it.copy(imageUri = uri) }

    fun submit(contentResolver: ContentResolver) {
        if (!canCreateZone()) {
            _state.update {
                it.copy(error = "Debe completar todos los campos requeridos y seleccionar exactamente 3 vigilantes")
            }
            return
        }

        // Validar que la ubicación esté dentro del radio permitido
        if (state.value.latitude.isNotBlank() && state.value.longitude.isNotBlank()) {
            val selectedLocation = LatLng(
                state.value.latitude.toDoubleOrNull() ?: 0.0,
                state.value.longitude.toDoubleOrNull() ?: 0.0
            )
            if (!isLocationWithinRadius(selectedLocation)) {
                _state.update {
                    it.copy(error = "La ubicación seleccionada está fuera del área permitida (${MAX_DISTANCE_METERS.toInt()}m)")
                }
                return
            }
        }

        _state.update { it.copy(isLoading = true, error = null, success = false) }

        Log.d(TAG, "Iniciando creación de zona segura")
        Log.d(TAG, "Usuarios seleccionados: ${buildUserIds()}")

        val fields = mutableMapOf<String, RequestBody>().apply {
            put("name", state.value.name.toPlainRequestBody())
            put("justification", state.value.justification.toPlainRequestBody())
            put("assumes_responsibility", state.value.assumesResponsibility.toString().toPlainRequestBody())
            put("latitude", state.value.latitude.toPlainRequestBody())
            put("longitude", state.value.longitude.toPlainRequestBody())
            put("status_id", state.value.statusId.toPlainRequestBody())
            put("user_ids", buildUserIds().toPlainRequestBody())
            put("rating", state.value.rating.ifBlank { "0" }.toPlainRequestBody())

            // Campos opcionales
            if (state.value.description.isNotBlank()) {
                put("description", state.value.description.toPlainRequestBody())
            }
            if (state.value.category.isNotBlank()) {
                put("category", state.value.category.toPlainRequestBody())
            }
        }

        var photoPart: MultipartBody.Part? = null
        state.value.imageUri?.let { uri ->
            Log.d(TAG, "Procesando imagen: $uri")
            photoPart = uriToPart("photo_url", uri, contentResolver)
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Enviando petición al backend")
                val result = createSafeZoneUseCase(fields, photoPart)
                Log.d(TAG, "Respuesta exitosa: $result")
                _state.update { it.copy(isLoading = false, success = true) }
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
}

// Estado de UI actualizado
data class SafeZoneUiState(
    val userSearchQuery: String = "",
    val userSuggestions: List<User> = emptyList(),
    val selectedUsers: List<User> = emptyList(),
    val name: String = "",
    val justification: String = "",
    val assumesResponsibility: Boolean = false,
    val latitude: String = "",
    val longitude: String = "",
    val statusId: String = "",
    val rating: String = "",
    val category: String = "",
    val description: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    // Nuevos campos para ubicación del usuario
    val currentUserLocation: LatLng? = null,
    val isLoadingUserLocation: Boolean = false
)