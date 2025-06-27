package com.jean.cuidemonosaqp.modules.userreviews.domain.repository

import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface UserReviewRepository {

    suspend fun getReviewsForUser(userId: String): NetworkResult<List<UserReview>>
}