package com.jean.cuidemonosaqp.features.profile.domain.model

data class PerfilUiState(
    val nombre: String = "María González",
    val miembroDesde: String = "Marzo 2023",
    val rating: Double = 4.7,
    val totalRatings: Int = 328,
    val puntosVigilados: Int = 12,
    val horasVigilancia: Int = 240,
    val confiabilidad: Int = 98
)