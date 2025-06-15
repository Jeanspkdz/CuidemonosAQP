package com.jean.cuidemonosaqp.modules.auth.data.model

//LOGIN
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)

// REGISTER
data class RegisterResponse(
    val id: Int,
    val dni: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val address: String,
    val reputation_status_id: Int,
    val dni_extension: String?,
    val dni_photo: String?,
    val profile_photo: String?,
    val createdAt: String,
    val updatedAt: String,
    val token_refresh: String?
)