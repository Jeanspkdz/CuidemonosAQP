package com.jean.cuidemonosaqp.modules.safeZone.domain.repository

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface SafeZoneRepository {
    suspend fun getAllSafeZones(): NetworkResult<List<SafeZoneResponseDTO>>
    suspend fun getSafeZoneById(id:String): NetworkResult<SafeZoneResponseDTO>
}