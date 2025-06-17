package com.jean.cuidemonosaqp.modules.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Data classes para el modelo
data class PerfilUiState(
    val nombre: String = "",
    val miembroDesde: String = "",
    val rating: Float = 0f,
    val totalRatings: Int = 0,
    val puntosVigilados: Int = 0,
    val horasVigilancia: Int = 0,
    val confiabilidad: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Resena(
    val id: String,
    val autor: String,
    val estrellas: Int,
    val comentario: String,
    val fecha: String
)

class ProfileViewModel : ViewModel() {

    private val _perfilUiState = MutableStateFlow(PerfilUiState())
    val perfilUiState: StateFlow<PerfilUiState> = _perfilUiState.asStateFlow()

    private val _resenas = MutableStateFlow<List<Resena>>(emptyList())
    val resenas: StateFlow<List<Resena>> = _resenas.asStateFlow()

    private val _eventos = MutableStateFlow<ProfileEvent?>(null)
    val eventos: StateFlow<ProfileEvent?> = _eventos.asStateFlow()

    init {
        cargarDatosPerfil()
        cargarResenas()
    }

    private fun cargarDatosPerfil() {
        viewModelScope.launch {
            _perfilUiState.value = _perfilUiState.value.copy(isLoading = true)

            try {
                // Simulación de datos - reemplazar con llamada real al repositorio
                val perfil = PerfilUiState(
                    nombre = "María González",
                    miembroDesde = "Enero 2023",
                    rating = 4.2f,
                    totalRatings = 28,
                    puntosVigilados = 12,
                    horasVigilancia = 145,
                    confiabilidad = 95,
                    isLoading = false
                )

                _perfilUiState.value = perfil

            } catch (e: Exception) {
                _perfilUiState.value = _perfilUiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar el perfil: ${e.message}"
                )
            }
        }
    }

    private fun cargarResenas() {
        viewModelScope.launch {
            try {
                // Simulación de datos - reemplazar con llamada real al repositorio
                val resenasList = listOf(
                    Resena(
                        id = "1",
                        autor = "Ana Pérez",
                        estrellas = 5,
                        comentario = "Excelente vigilante, muy responsable y atenta. Siempre reporta cualquier situación sospechosa.",
                        fecha = "15 de Mayo, 2024"
                    ),
                    Resena(
                        id = "2",
                        autor = "Carlos Ruiz",
                        estrellas = 4,
                        comentario = "Muy buena comunicación y puntual en sus turnos. Recomendada.",
                        fecha = "8 de Mayo, 2024"
                    ),
                    Resena(
                        id = "3",
                        autor = "Luis Torres",
                        estrellas = 5,
                        comentario = "Persona de confianza, ha estado vigilando nuestra zona por meses sin problemas.",
                        fecha = "2 de Mayo, 2024"
                    ),
                    Resena(
                        id = "4",
                        autor = "Patricia Silva",
                        estrellas = 4,
                        comentario = "Buen trabajo en general, muy comprometida con la seguridad del barrio.",
                        fecha = "28 de Abril, 2024"
                    )
                )

                _resenas.value = resenasList

            } catch (e: Exception) {
                _perfilUiState.value = _perfilUiState.value.copy(
                    error = "Error al cargar las reseñas: ${e.message}"
                )
            }
        }
    }

    fun onCalificarUsuario() {
        viewModelScope.launch {
            _eventos.value = ProfileEvent.MostrarDialogoCalificacion
        }
    }

    fun onContactoSeguro() {
        viewModelScope.launch {
            _eventos.value = ProfileEvent.AbrirContactoSeguro
        }
    }

    fun calificarUsuario(estrellas: Int, comentario: String) {
        viewModelScope.launch {
            try {
                // Aquí iría la llamada al repositorio para enviar la calificación
                // repository.calificarUsuario(userId, estrellas, comentario)

                // Actualizar el estado local después de la calificación exitosa
                val perfilActual = _perfilUiState.value
                val nuevoTotalRatings = perfilActual.totalRatings + 1
                val nuevoRating = ((perfilActual.rating * perfilActual.totalRatings) + estrellas) / nuevoTotalRatings

                _perfilUiState.value = perfilActual.copy(
                    rating = nuevoRating,
                    totalRatings = nuevoTotalRatings
                )

                // Recargar las reseñas para mostrar la nueva
                cargarResenas()

                _eventos.value = ProfileEvent.CalificacionEnviada

            } catch (e: Exception) {
                _eventos.value = ProfileEvent.Error("Error al enviar calificación: ${e.message}")
            }
        }
    }

    fun iniciarContactoSeguro() {
        viewModelScope.launch {
            try {
                // Aquí iría la lógica para iniciar contacto seguro
                // repository.iniciarContactoSeguro(userId)

                _eventos.value = ProfileEvent.ContactoIniciado

            } catch (e: Exception) {
                _eventos.value = ProfileEvent.Error("Error al iniciar contacto: ${e.message}")
            }
        }
    }

    fun limpiarEvento() {
        _eventos.value = null
    }

    fun limpiarError() {
        _perfilUiState.value = _perfilUiState.value.copy(error = null)
    }

    fun recargarPerfil() {
        cargarDatosPerfil()
        cargarResenas()
    }
}

sealed class ProfileEvent {
    object MostrarDialogoCalificacion : ProfileEvent()
    object AbrirContactoSeguro : ProfileEvent()
    object CalificacionEnviada : ProfileEvent()
    object ContactoIniciado : ProfileEvent()
    data class Error(val mensaje: String) : ProfileEvent()
}