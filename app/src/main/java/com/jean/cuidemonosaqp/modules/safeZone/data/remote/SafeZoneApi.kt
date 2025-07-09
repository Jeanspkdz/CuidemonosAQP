package com.jean.cuidemonosaqp.modules.safeZone.data.remote

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.data.model.SafeZoneResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

interface SafeZoneApi {
    @GET("/safezones")
    suspend fun getAllSafeZones(): Response<List<SafeZoneResponseDTO>>

<<<<<<< HEAD
    @GET("/safezones/{id}")
    suspend fun getSafeZoneById(@Path("id") id : String): Response<SafeZoneResponseDTO>
=======
    @Multipart
    @POST("safezones")
    suspend fun createSafeZoneWithImage(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("justification") justification: RequestBody,
        @Part("assumes_responsibility") assumesResponsibility: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("status_id") statusId: RequestBody,
        @Part("user_ids") userIds: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part photo_url: MultipartBody.Part?
    ): SafeZoneResponse
>>>>>>> ba19bfb (Se integra la funcionalidad para crear zonas)
}