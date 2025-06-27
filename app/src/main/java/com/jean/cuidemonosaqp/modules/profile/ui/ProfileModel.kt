package com.jean.cuidemonosaqp.modules.profile.ui

data class UserUI(
    val id: Int,
    val dni: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val profilePhotoUrl: String,
    val memberSince: String,
    val rating: Double = 4.0,
    val monitoredPoints: Int = 4,
    val surveillanceHours: Int = 12,
    val reliability: Int = 90
) {
    val fullName: String
        get() = "$firstName $lastName"
}

data class ReviewUI(
    val id: String,
    val author: String,
    val stars: Int,
    val comment: String,
    val date: String
)