package com.jean.cuidemonosaqp.modules.profile.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val getUserInfoUseCase: GetUserInfoUseCase) :
    ViewModel() {

    companion object {
        const val TAG = "ProfileViewModel"
    }

    private val _profileUiState = MutableStateFlow<Profile?>(null)
    val profileUiState: StateFlow<Profile?> = _profileUiState.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _events = MutableStateFlow<ProfileEvent?>(null)
    val events: StateFlow<ProfileEvent?> = _events.asStateFlow()

    init {
        loadProfileData()
        loadReviews()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                _isLoading.update { true }

                when (val response = getUserInfoUseCase()) {
                    is NetworkResult.Error -> {
                        Log.d(TAG, "loadProfileData Error ${response.message}")
                        _errorMessage.update { response.message }
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "loadProfileData Success ${response.data.toProfile()}")
                        _profileUiState.update { response.data.toProfile() }
                    }
                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }

            } catch (e: Exception) {
                // Handle Exception
                Log.d(TAG, "ERROR :  ${e.message}")
                _errorMessage.update { "Something went wrong" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            val mockReviews = listOf(
                Review(
                    id = "1",
                    author = "María González",
                    stars = 5,
                    comment = "Excelente servicio, muy amable y puntual.",
                    date = "2025-06-01"
                ),
                Review(
                    id = "2",
                    author = "Carlos Herrera",
                    stars = 4,
                    comment = "Buena atención aunque tardó un poco.",
                    date = "2025-06-10"
                ),
                Review(
                    id = "3",
                    author = "Lucía Pérez",
                    stars = 3,
                    comment = "Regular, esperaba algo más profesional.",
                    date = "2025-06-15"
                ),
                Review(
                    id = "4",
                    author = "Juan Díaz",
                    stars = 5,
                    comment = "Muy recomendable, volveré a contratar.",
                    date = "2025-06-17"
                )
            )

            _reviews.value = mockReviews
        }
    }

    fun onCalificarUsuario() {
        viewModelScope.launch {
        }
    }

    fun onContactoSeguro() {
        viewModelScope.launch {
        }
    }

    fun calificarUsuario(estrellas: Int, comentario: String) {
        viewModelScope.launch {

        }
    }

    fun iniciarContactoSeguro() {
        viewModelScope.launch {
        }
    }

    fun limpiarEvento() {
    }

    fun limpiarError() {

    }

    fun recargarPerfil() {
    }
}

sealed class ProfileEvent {
    object MostrarDialogoCalificacion : ProfileEvent()
    object AbrirContactoSeguro : ProfileEvent()
    object CalificacionEnviada : ProfileEvent()
    object ContactoIniciado : ProfileEvent()
    data class Error(val mensaje: String) : ProfileEvent()
}