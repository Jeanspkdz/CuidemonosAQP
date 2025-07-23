package com.jean.cuidemonosaqp.modules.user.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    val id: Int,
    val dni: String,
    @SerializedName("dni_extension") val dniExtension: String?,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    @SerializedName("address_latitude") val addressLatitude: Double?,
    @SerializedName("address_longitude") val addressLongitude: Double?,
    @SerializedName("dni_photo_url") val dniPhotoUrl: String?,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String?,
    @SerializedName("reputation_score") val reputationScore: Int?,
    @SerializedName("reputation_status_id") val reputationStatusId: Int?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
