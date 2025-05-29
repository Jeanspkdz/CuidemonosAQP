package com.jean.cuidemonosaqp.features.auth.domain

interface AuthRepository {
    suspend fun login(identifier: String, password: String): Result<String> // solo token por ahora
}