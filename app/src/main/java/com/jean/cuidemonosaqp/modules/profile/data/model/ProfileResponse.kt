package com.jean.cuidemonosaqp.modules.profile.data.model

data class UserInfoResponse(
    val id: Int,
    val dni: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val address: String,
    val dni_photo_url: String,
    val profile_photo_url: String,
    val reputation_status_id: Int,
    val createdAt: String,
    val updatedAt: String
)