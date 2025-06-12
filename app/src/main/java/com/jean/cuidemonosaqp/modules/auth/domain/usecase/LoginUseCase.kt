package com.jean.cuidemonosaqp.modules.auth.domain.usecase

import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(identifier: String, password: String) =
        repository.login(identifier, password)
}