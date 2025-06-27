package com.jean.cuidemonosaqp.modules.user.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int,
    val dni: String,
    @SerializedName("dni_extension") val dniExtension: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    @SerializedName("dni_photo_url") val dniPhotoUrl: String,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
