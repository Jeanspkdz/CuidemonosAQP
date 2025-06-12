/*package com.jean.cuidemonosaqp.modules.auth.data

import java.sql.Timestamp

// RESPONSE
data class User (
    val accessToken: String
)

// REQUEST
data class LoginRequest(
    val identifier: String,
    val password: String
)

// RESPONSE
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

// REQUEST
data class RegisterRequest(
    val dni: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val dni_extension: String?,
)

// RESPONSE
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

*/