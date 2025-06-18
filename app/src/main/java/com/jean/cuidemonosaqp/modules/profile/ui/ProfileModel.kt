package com.jean.cuidemonosaqp.modules.profile.ui

data class Profile(
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
    val monitoredPoints: Int = 0,
    val surveillanceHours: Int = 0,
    val reliability: Int = 0
) {
    val fullName: String
        get() = "$firstName $lastName"
}

data class Review(
    val id: String,
    val author: String,
    val stars: Int,
    val comment: String,
    val date: String
)