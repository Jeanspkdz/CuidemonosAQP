package com.jean.cuidemonosaqp.modules.profile.data.remote

import com.jean.cuidemonosaqp.modules.profile.data.model.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {

    @GET("/auth/me")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}