package com.jean.cuidemonosaqp.features.profile.presentation

import androidx.lifecycle.ViewModel
import com.jean.cuidemonosaqp.features.profile.domain.model.PerfilUiState
import com.jean.cuidemonosaqp.features.profile.domain.model.Resena
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PerfilViewModel : ViewModel() {

    private val _perfilUiState = MutableStateFlow(PerfilUiState())
    val perfilUiState: StateFlow<PerfilUiState> = _perfilUiState

    private val _resenas = MutableStateFlow(
        listOf(
            Resena("Carlos Ruiz", "Excelente vigilante, muy responsable y atento.", 5, "Hace 2 días"),
            Resena("Ana Martínez", "Muy buena comunicación y puntualidad.", 4, "Hace 1 semana")
        )
    )
    val resenas: StateFlow<List<Resena>> = _resenas
}