package com.jean.cuidemonosaqp.modules.safeZone.data.dto

import com.google.gson.annotations.SerializedName
import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto

data class SafeZoneResponseDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val category: String,
    val justification: String?,

    @SerializedName("photo_url")
    val photoUrl: String?,

    @SerializedName("assumes_responsibility")
    val assumesResponsibility: Boolean,

    val longitude: Double,
    val latitude: Double,

    @SerializedName("status_id")
    val statusId: Int,

    @SerializedName("is_active")
    val isActive: Boolean,

    val rating: Float?,
    val createdAt: String,

    val status: StatusResponseDTO,
    val users: List<UserResponseDto>
)

data class StatusResponseDTO(
    val id: Int,
    val status: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)