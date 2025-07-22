package com.jean.cuidemonosaqp.modules.userreviews.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewRequestDTO
import com.jean.cuidemonosaqp.modules.userreviews.data.mapper.toDomain
import com.jean.cuidemonosaqp.modules.userreviews.data.remote.UserReviewApi
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class UserReviewRepositoryImp @Inject constructor(
    private val userReviewApi: UserReviewApi
) : UserReviewRepository {

    companion object {
        const val TAG = "UserReviewRep"
    }

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

    override suspend fun addReview(
        reviewerId: Int,
        reviewedId: Int,
        comment: String,
        score: Int
    ): NetworkResult<UserReview> {
        return  try {
            val userReviewRequestDTO = UserReviewRequestDTO(
                reviewerId = reviewerId,
                reviewedId = reviewedId,
                comment= comment,
                score = score
            )
            val response = userReviewApi.addUserReview(userReviewRequestDTO)
            val responseBody = response.body()
            if(response.isSuccessful && responseBody !== null){
                NetworkResult.Success(responseBody.toDomain())
            }else {
                Log.d(TAG, "addReview Not Successful: ${response.code()} ")
                NetworkResult.Error("No se pudo crear la Solicitud")
            }
        }catch (e: Exception){
            Log.d(TAG, "Error: ${e.message} ")
            NetworkResult.Error("Algo Salio Mal")
        }
    }
}