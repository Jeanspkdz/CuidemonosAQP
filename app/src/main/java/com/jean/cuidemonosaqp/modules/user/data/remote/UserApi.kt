package com.jean.cuidemonosaqp.modules.user.data.remote

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: String):Response<UserResponse>
}