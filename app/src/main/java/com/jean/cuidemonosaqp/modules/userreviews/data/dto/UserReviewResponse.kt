package com.jean.cuidemonosaqp.modules.userreviews.data.dto

import com.google.gson.annotations.SerializedName

data class UserReviewResponse(
    val id: Int,
    val comment: String,

    @SerializedName("reviewer_id")
    val reviewerId: Int,

    @SerializedName("reviewed_id")
    val reviewedId: Int,

    val score: Int,

    @SerializedName("is_public")
    val isPublic: Boolean,

    @SerializedName("report_count")
    val reportCount: Int,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    val reviewer: Reviewer
)

data class Reviewer(
    val id: Int,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String
)