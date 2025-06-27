package com.jean.cuidemonosaqp.modules.userreviews.data.repository

import com.jean.cuidemonosaqp.modules.userreviews.data.mapper.toDomain
import com.jean.cuidemonosaqp.modules.userreviews.data.remote.UserReviewApi
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class UserReviewRepositoryImp @Inject constructor(
    private val userReviewApi: UserReviewApi
) : UserReviewRepository {
    override suspend fun getReviewsForUser(userId: String): NetworkResult<List<UserReview>> {
        return try {
            val response = userReviewApi.getUserReviewsForUser(userId = userId)
            val responseBody = response.body()
            if(response.isSuccessful && responseBody !== null){
                val userReviews= responseBody.map { it.toDomain() }
                NetworkResult.Success(userReviews)
            }else {
                NetworkResult.Error("Hubo Problemas")
            }
        } catch (e: Exception){
            NetworkResult.Error("Algo Salio Mal")
        }
    }
}