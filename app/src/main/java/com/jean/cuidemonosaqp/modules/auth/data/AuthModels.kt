package com.jean.cuidemonosaqp.modules.auth.data

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
