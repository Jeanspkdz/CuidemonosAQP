package com.jean.cuidemonosaqp.modules.userreviews.data.mapper

import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewResponse
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.ReviewerInfo
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview


fun UserReviewResponse.toDomain() : UserReview {
    return UserReview(
        id = id,
        comment = comment,
        reviewerId = reviewerId,
        reviewedId = reviewedId,
        score = score,
        isPublic = isPublic,
        reportCount = reportCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
        reviewer = ReviewerInfo
            (
            id = reviewer.id,
            firstName = reviewer.firstName,
            lastName = reviewer.lastName,
            profilePhotoUrl = reviewer.profilePhotoUrl
        )
    )
}