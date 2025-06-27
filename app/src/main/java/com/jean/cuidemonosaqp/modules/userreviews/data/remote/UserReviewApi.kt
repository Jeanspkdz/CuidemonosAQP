package com.jean.cuidemonosaqp.modules.userreviews.data.remote

import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserReviewApi {

    @GET("/user-review/{userId}")
    suspend fun getUserReviewsForUser(@Path("userId") userId: String): Response<List<UserReviewResponse>>
}