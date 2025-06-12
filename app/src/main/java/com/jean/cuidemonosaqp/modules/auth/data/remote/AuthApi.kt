package com.jean.cuidemonosaqp.modules.auth.data.remote

import com.jean.cuidemonosaqp.modules.auth.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("dni") dni: RequestBody,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("dni_extension") dniExtension: RequestBody?, // opcional
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("reputation_status_id") reputationStatusId: RequestBody,
        @Part dniPhoto: MultipartBody.Part?,
        @Part profilePhoto: MultipartBody.Part?
    ): RegisterResponse
}