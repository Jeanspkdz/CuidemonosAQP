package com.jean.cuidemonosaqp.modules.profile.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetReviewsForUserUseCase
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val getUserInfoUseCase: GetUserInfoUseCase,
    val getUserReviewsUseCase: GetReviewsForUserUseCase
) :
    ViewModel() {

    companion object {
        const val TAG = "ProfileViewModel"
    }

    private val _userState = MutableStateFlow<UserUI?>(null)
    val userState: StateFlow<UserUI?> = _userState.asStateFlow()

    private val _reviews = MutableStateFlow<List<ReviewUI>>(emptyList())
    val reviews: StateFlow<List<ReviewUI>> = _reviews.asStateFlow()

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

                //TODO : Change for real ID
                when (val response = getUserInfoUseCase(1.toString())) {
                    is NetworkResult.Error -> {
                        Log.d(TAG, "loadProfileData Error ${response.message}")
                        _errorMessage.update { response.message }
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "loadProfileData Success ${response.data}")
                        val data = response.data
                        _userState.update { data.toUI() }
                    }

                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }

            } catch (e: Exception) {
                // Handle Exception
                Log.d(TAG, "ERROR :  ${e.message}")
                _errorMessage.update { "Something went wrong." }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            try {
                when (val response = getUserReviewsUseCase(1.toString())) {
                    is NetworkResult.Error -> {
                        Log.d(TAG, "loadProfileData Error ${response.message}")
                        _errorMessage.update { response.message }
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "loadProfileData Success ${response.data}")
                        val data = response.data
                        _reviews.update { data.map { it.toUI() } }
                    }

                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }
            } catch (e: Throwable) {
                Log.d(TAG, "ERROR :  ${e.message}")
                _errorMessage.update { "Something went wrong." }
            }
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