package com.jean.cuidemonosaqp.modules.safeZone.data.remote

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import retrofit2.Response
import retrofit2.http.GET

interface SafeZoneApi {
    @GET("/safezones")
    suspend fun getAllSafeZones(): Response<List<SafeZoneResponseDTO>>
}