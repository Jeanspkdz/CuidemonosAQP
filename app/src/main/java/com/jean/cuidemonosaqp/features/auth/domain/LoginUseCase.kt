package com.jean.cuidemonosaqp.features.auth.domain

import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(identifier: String, password: String): Result<String> {
        return repository.login(identifier, password)
    }
}