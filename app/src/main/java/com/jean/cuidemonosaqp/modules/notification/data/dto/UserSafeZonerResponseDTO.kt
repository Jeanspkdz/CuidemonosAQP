package com.jean.cuidemonosaqp.modules.notification.data.dto

import com.google.gson.annotations.SerializedName

data class UserSafeZoneInvitationResponseDto(
    val id: Int,

    @SerializedName("safe_zone_id")
    val safeZoneId: Int,

    @SerializedName("user_id")
    val userId: Int,

    val status: String,

    @SerializedName("confirmed_at")
    val confirmedAt: String?,

    @SerializedName("is_seen")
    val isSeen: Boolean,

    @SerializedName("safeZone")
    val safeZone: SafeZoneResponseDto,

    @SerializedName("user")
    val user: UserResponseDto
)

data class SafeZoneResponseDto(
    val id: Int,
    val name: String,
    val description: String,

    @SerializedName("photo_url")
    val photoUrl: String,

    val latitude: Double,
    val longitude: Double,
    val rating: Int?
)

data class UserResponseDto(
    val id: Int,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String,

    @SerializedName("reputation_score")
    val reputationScore: Int
)
