package com.jean.cuidemonosaqp.modules.userreviews.domain.usecase

import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewRequestDTO
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import javax.inject.Inject

class CreateUserReviewUseCase @Inject constructor(
    private val userReviewRepository: UserReviewRepository
) {

    suspend operator fun invoke(
        reviewerId: Int,
        reviewedId: Int,
        comment: String,
        score: Int
    ) =
        userReviewRepository.addReview(
            reviewerId,
            reviewedId,
            comment,
            score,
        )
}