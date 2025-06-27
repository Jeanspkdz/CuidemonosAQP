package com.jean.cuidemonosaqp.modules.profile.domain.repository

import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface ProfileRepository {
    suspend fun getUserInformation(id: String) : NetworkResult<User>
    suspend fun getUserReviews(userId: String) : NetworkResult<List<UserReview>>
}