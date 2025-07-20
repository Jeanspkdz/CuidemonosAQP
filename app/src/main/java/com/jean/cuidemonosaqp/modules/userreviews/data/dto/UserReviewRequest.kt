package com.jean.cuidemonosaqp.modules.userreviews.data.dto

import com.google.gson.annotations.SerializedName

data class UserReviewRequestDTO(
    @SerializedName("reviewer_id") val reviewerId: Int,
    @SerializedName("reviewed_id") val reviewedId: Int,
    val comment: String,
    val score: Int
)