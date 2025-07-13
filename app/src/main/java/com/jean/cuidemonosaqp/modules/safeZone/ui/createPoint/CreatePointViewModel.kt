package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.CreateSafeZoneUseCase
import com.jean.cuidemonosaqp.shared.utils.toPlainRequestBody
import com.jean.cuidemonosaqp.shared.utils.uriToPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateSafeZoneViewModel @Inject constructor(
    private val createSafeZoneUseCase: CreateSafeZoneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SafeZoneUiState())
    val state = _state.asStateFlow()

    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onJustificationChange(value: String) = _state.update { it.copy(justification = value) }
    fun onResponsibilityChange(value: Boolean) = _state.update { it.copy(assumesResponsibility = value) }
    fun onLatitudeChange(value: String) = _state.update { it.copy(latitude = value) }
    fun onLongitudeChange(value: String) = _state.update { it.copy(longitude = value) }
    fun onStatusIdChange(value: String) = _state.update { it.copy(statusId = value) }
    fun onUserIdsChange(value: String) = _state.update { it.copy(userIds = value) }
    fun onRatingChange(value: String) = _state.update { it.copy(rating = value) }
    fun onCategoryChange(value: String) = _state.update { it.copy(category = value) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }
    fun onImageUriChange(uri: Uri?) = _state.update { it.copy(imageUri = uri) }

    fun submit(contentResolver: ContentResolver) {
        _state.update { it.copy(isLoading = true, error = null, success = false) }

        Log.d("CreateSafeZoneViewModel", "Iniciando creación de zona segura")
        Log.d("CreateSafeZoneViewModel", "Datos: ${state.value}")

        // Validar datos requeridos
        if (state.value.name.isBlank() || state.value.justification.isBlank() ||
            state.value.latitude.isBlank() || state.value.longitude.isBlank() ||
            state.value.statusId.isBlank() || state.value.userIds.isBlank()) {
            _state.update { it.copy(isLoading = false, error = "Todos los campos requeridos deben ser completados") }
            return
        }

        val fields = mutableMapOf<String, RequestBody>().apply {
            put("name", state.value.name.toPlainRequestBody())
            put("justification", state.value.justification.toPlainRequestBody())
            put("assumes_responsibility", state.value.assumesResponsibility.toString().toPlainRequestBody())
            put("latitude", state.value.latitude.toPlainRequestBody())
            put("longitude", state.value.longitude.toPlainRequestBody())
            put("status_id", state.value.statusId.toPlainRequestBody())
            put("user_ids", state.value.userIds.toPlainRequestBody())
            put("rating", state.value.rating.ifBlank { "0" }.toPlainRequestBody())

            // Agregar campos opcionales solo si tienen contenido
            if (state.value.description.isNotBlank()) {
                put("description", state.value.description.toPlainRequestBody())
            }
            if (state.value.category.isNotBlank()) {
                put("category", state.value.category.toPlainRequestBody())
            }
        }

        Log.d("CreateSafeZoneViewModel", "Campos preparados: ${fields.keys}")

        var photoPart: MultipartBody.Part? = null
        state.value.imageUri?.let { uri ->
            Log.d("CreateSafeZoneViewModel", "Procesando imagen: $uri")
            photoPart = uriToPart("photo_url", uri, contentResolver)
            if (photoPart != null) {
                Log.d("CreateSafeZoneViewModel", "Imagen procesada correctamente")
            } else {
                Log.e("CreateSafeZoneViewModel", "Error al procesar imagen")
            }
        }

        viewModelScope.launch {
            try {
                Log.d("CreateSafeZoneViewModel", "Enviando petición al backend")
                val result = createSafeZoneUseCase(fields, photoPart)
                Log.d("CreateSafeZoneViewModel", "Respuesta exitosa: $result")
                _state.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                Log.e("CreateSafeZoneViewModel", "Error al crear zona segura", e)
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }
}

// Estado de UI
data class SafeZoneUiState(
    val name: String = "",
    val justification: String = "",
    val assumesResponsibility: Boolean = false,
    val latitude: String = "",
    val longitude: String = "",
    val statusId: String = "",
    val userIds: String = "",
    val rating: String = "",
    val category: String = "",
    val description: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

