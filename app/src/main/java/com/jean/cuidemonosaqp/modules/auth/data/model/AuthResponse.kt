package com.jean.cuidemonosaqp.modules.auth.data.model
import com.google.gson.annotations.SerializedName

//LOGIN
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val id:String
)

data class RegisterResponse(
    val id: Int,
    val dni: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    @SerializedName("address_latitude")
    val addressLatitude: Double,
    @SerializedName("address_longitude")
    val addressLongitude: Double,
    @SerializedName("reputation_status_id")
    val reputationStatusId: Int,
    @SerializedName("dni_extension")
    val dniExtension: String?,
    @SerializedName("dni_photo_url")
    val dniPhotoUrl: String?,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String?,
    val createdAt: String,
    val updatedAt: String,
    val token_refresh: String?
)
