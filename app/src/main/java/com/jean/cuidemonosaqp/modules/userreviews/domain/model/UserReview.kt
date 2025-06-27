package com.jean.cuidemonosaqp.modules.userreviews.domain.model

data class UserReview(
    val id: Int,
    val comment: String,
    val reviewerId: Int,
    val reviewedId: Int,
    val score: Int,
    val isPublic: Boolean,
    val reportCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val reviewer: ReviewerInfo
)

data class ReviewerInfo(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val profilePhotoUrl: String
)