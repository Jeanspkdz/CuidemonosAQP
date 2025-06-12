package com.jean.cuidemonosaqp.modules.auth.data.model

//LOGIN
data class LoginRequest(
    val identifier: String,
    val password: String
)

//REGISTER
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
