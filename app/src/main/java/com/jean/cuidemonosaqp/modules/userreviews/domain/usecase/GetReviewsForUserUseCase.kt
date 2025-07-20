package com.jean.cuidemonosaqp.modules.userreviews.domain.usecase

import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import javax.inject.Inject

class GetReviewsForUserUseCase @Inject constructor(
   private val userReviewRepository: UserReviewRepository
) {

    suspend operator fun invoke(userId: String) = userReviewRepository.getReviewsForUser(userId)

}