package com.jean.cuidemonosaqp.modules.safeZone.data.dto

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto

data class SafeZoneResponseDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val category: String,
    val justification: String?,
    val photo_url: String?,
    val assumes_responsibility: Boolean,
    val longitude: Double,
    val latitude: Double,
    val status_id: Int,
    val is_active: Boolean,
    val rating: Float?,
    val createdAt: String,
    val status: StatusResponseDTO,
    val Users: List<UserResponseDto>
)

data class StatusResponseDTO(
    val id: Int,
    val status: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)