package com.jean.cuidemonosaqp.modules.user.data.dto

import com.google.gson.annotations.SerializedName

data class ReputationStatusDto(
    val id: Int,
    val status: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)