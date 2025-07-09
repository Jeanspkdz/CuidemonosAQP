package com.jean.cuidemonosaqp.modules.safeZone.data.remote

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface SafeZoneApi {
    @GET("/safezones")
    suspend fun getAllSafeZones(): Response<List<SafeZoneResponseDTO>>

    @GET("/safezones/{id}")
    suspend fun getSafeZoneById(@Path("id") id : String): Response<SafeZoneResponseDTO>
}