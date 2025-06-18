package com.jean.cuidemonosaqp.modules.profile.domain.usecase

import com.jean.cuidemonosaqp.modules.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke() = repository.getUserInfo()
}