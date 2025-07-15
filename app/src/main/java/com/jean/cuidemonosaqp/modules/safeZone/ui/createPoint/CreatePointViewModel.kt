package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.CreateSafeZoneUseCase
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.user.domain.usecase.SearchUsersUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.utils.toPlainRequestBody
import com.jean.cuidemonosaqp.shared.utils.uriToPart
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

@HiltViewModel
class CreateSafeZoneViewModel @Inject constructor(
    private val createSafeZoneUseCase: CreateSafeZoneUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "CreateSafeZoneVM"
        private const val MAX_SELECTED_USERS = 3
        private const val MAX_SUGGESTIONS = 5
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
                !state.isLoading
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
    val error: String? = null
)