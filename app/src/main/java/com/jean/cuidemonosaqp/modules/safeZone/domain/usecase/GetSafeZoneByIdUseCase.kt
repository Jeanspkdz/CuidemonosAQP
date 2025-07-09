package com.jean.cuidemonosaqp.modules.safeZone.domain.usecase

import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import javax.inject.Inject

class GetSafeZoneByIdUseCase @Inject constructor(
    private val safeZoneRepository: SafeZoneRepository
){
    suspend operator fun invoke(id: String) = safeZoneRepository.getSafeZoneById(id)
}