package com.jean.cuidemonosaqp.modules.user.domain.model


data class User(
    val id: Int,
    val dni: String,
    val dniExtension: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val address: String,
    val dniPhotoUrl: String,
    val profilePhotoUrl: String,
//    val reputation: Reputation,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}