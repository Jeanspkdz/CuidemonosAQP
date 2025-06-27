package com.jean.cuidemonosaqp.modules.profile.ui

import android.annotation.SuppressLint
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.userreviews.domain.model.UserReview
import com.jean.cuidemonosaqp.shared.utils.formatDate

@SuppressLint("NewApi")
fun User.toUI(): UserUI = UserUI(
    id = id,
    dni = dni,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address,
    profilePhotoUrl = profilePhotoUrl,
    memberSince = formatDate(createdAt)
)

fun UserReview.toUI(): ReviewUI = ReviewUI(
    id = id.toString(),
    author = "${reviewer.firstName} ${reviewer.lastName}",
    stars = score,
    comment = comment,
    date = createdAt.substring(0, 10)
)