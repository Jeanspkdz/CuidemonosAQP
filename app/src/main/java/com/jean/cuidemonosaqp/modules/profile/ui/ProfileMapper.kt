package com.jean.cuidemonosaqp.modules.profile.ui

import android.annotation.SuppressLint
import com.jean.cuidemonosaqp.modules.profile.data.model.UserInfoResponse
import com.jean.cuidemonosaqp.shared.utils.formatDate

@SuppressLint("NewApi")
fun UserInfoResponse.toProfile(): Profile = Profile(
    id = id,
    dni = dni,
    firstName = first_name,
    lastName = last_name,
    phone = phone,
    email = email,
    address = address,
    profilePhotoUrl = profile_photo_url,
    memberSince = formatDate(createdAt)
)

