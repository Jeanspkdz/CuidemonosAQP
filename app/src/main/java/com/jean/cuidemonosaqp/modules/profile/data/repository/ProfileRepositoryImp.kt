package com.jean.cuidemonosaqp.modules.profile.data.repository

import com.jean.cuidemonosaqp.modules.profile.domain.repository.ProfileRepository
import com.jean.cuidemonosaqp.modules.user.domain.repository.UserRepository
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class ProfileRepositoryImp @Inject constructor(
    private val userReviewRepository: UserReviewRepository,
    private val userRepository: UserRepository,
) : ProfileRepository {

    companion object {
        const val TAG = "ProfileRepImp"
    }

    override suspend fun getUserInformation(id:String) : NetworkResult<User> {
        return userRepository.getUserById(id)
    }

    override suspend fun getUserReviews(userId:String): NetworkResult<List<UserReview>> {
      return userReviewRepository.getReviewsForUser(userId)
    }
}