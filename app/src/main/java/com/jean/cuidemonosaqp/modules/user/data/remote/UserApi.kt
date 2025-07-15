package com.jean.cuidemonosaqp.modules.user.data.remote

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/users")
    suspend fun searchUsers(
        @Query("search") query: String?
    ): Response<List<UserResponseDto>>

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<UserResponseDto>
}