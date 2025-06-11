package com.jean.cuidemonosaqp.modules.auth.data

interface AuthRepository {
    suspend fun login(identifier: String, password: String): LoginResponse
}