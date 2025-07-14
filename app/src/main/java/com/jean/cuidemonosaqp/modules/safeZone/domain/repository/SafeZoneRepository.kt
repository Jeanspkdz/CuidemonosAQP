package com.jean.cuidemonosaqp.modules.safeZone.domain.repository

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.modules.safeZone.data.model.SafeZoneResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SafeZoneRepository {
    suspend fun getAllSafeZones(): NetworkResult<List<SafeZoneResponseDTO>>
    suspend fun getSafeZoneById(id:String): NetworkResult<SafeZoneResponseDTO>
    suspend fun createSafeZoneWithImage(
        fields: Map<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ): SafeZoneResponse
}