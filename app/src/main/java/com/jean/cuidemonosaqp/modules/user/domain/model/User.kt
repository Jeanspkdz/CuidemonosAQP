package com.jean.cuidemonosaqp.modules.user.domain.model

data class User(
    val id: Int,
    val dni: String,
    val dniExtension: String?,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val addressLatitude: Double?,
    val addressLongitude: Double?,
    val dniPhotoUrl: String?,
    val profilePhotoUrl: String?,
    val reputationScore: Int?,
    val reputationStatusId: Int?,
    val isActive: Boolean,
    val refreshToken: String?,
    val createdAt: String,
    val updatedAt: String
) {
    val fullName: String
        get() = "$firstName $lastName"

    // Método de utilidad para verificar si tiene coordenadas válidas
    val hasValidCoordinates: Boolean
        get() = addressLatitude != null && addressLongitude != null

    // Método para obtener las coordenadas como Pair (útil para mapas)
    val coordinates: Pair<Double, Double>?
        get() = if (hasValidCoordinates) {
            Pair(addressLatitude!!, addressLongitude!!)
        } else null
}