package com.jean.cuidemonosaqp.modules.userreviews.data.remote

import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewRequestDTO
import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserReviewApi {

    @GET("/user-review/{userId}")
    suspend fun getUserReviewsForUser(@Path("userId") userId: String): Response<List<UserReviewResponseDTO>>

    @POST("/user-review")
    suspend fun addUserReview(@Body userReviewRequestDto: UserReviewRequestDTO): Response<UserReviewResponseDTO>
}