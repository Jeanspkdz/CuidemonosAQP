package com.jean.cuidemonosaqp.modules.safeZone.data.model
import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa la respuesta del backend al crear o consultar una zona segura.
 */
data class SafeZoneResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val category: String?,
    val justification: String,
    val photo_url: String?,
    val assumes_responsibility: Boolean,
    val longitude: Double,
    val latitude: Double,
    val status_id: Int,
    val is_active: Boolean,
    val rating: Float,
    val createdAt: String,
    val status: Status?,
    @SerializedName("Users")
    val users: List<SafeUser>
)

/**
 * Estado asociado a la zona segura (ej. SAFE, DANGER).
 */
data class Status(
    val id: Int,
    val status: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Representa a un usuario que participa en la validación de la zona segura.
 */
data class SafeUser(
    val id: Int,
    val dni: String,
    val first_name: String,
    val last_name: String,
    val dni_extension: String?,
    val address: String?,
    val dni_photo_url: String?,
    val profile_photo_url: String?,
    val phone: String,
    val email: String,
    val reputation_score: Int,
    val reputation_status_id: Int,
    val is_active: Boolean,
    val createdAt: String,
    val updatedAt: String
)