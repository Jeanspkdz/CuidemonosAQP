package com.jean.cuidemonosaqp.modules.safeZone.domain.usecase

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class GetAllSafeZonesUseCase @Inject constructor(
    private val repository:SafeZoneRepository
) {
    suspend operator fun invoke(): NetworkResult<List<SafeZoneResponseDTO>> {
        return repository.getAllSafeZones()
    }
}