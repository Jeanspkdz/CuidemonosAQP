package com.jean.cuidemonosaqp.modules.safeZone.data.remote

import com.jean.cuidemonosaqp.modules.safeZone.data.model.SafeZoneResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SafeZoneApi {

    @Multipart
    @POST("safe_zones")
    suspend fun createSafeZone(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("justification") justification: RequestBody,
        @Part("photo_url") photoUrl: MultipartBody.Part,
        @Part("assumes_responsibility") assumesResponsibility: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("status_id") statusId: RequestBody,
        @Part("user_ids") userIds: RequestBody
    ): Response<SafeZoneResponse>
}
