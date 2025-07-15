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
    // Manejar nulos en URLs de foto de perfil
    profilePhotoUrl = profilePhotoUrl.orEmpty(),
    memberSince = formatDate(createdAt)
)

@SuppressLint("NewApi")
fun UserReview.toUI(): ReviewUI = ReviewUI(
    id = id.toString(),
    author = "${reviewer.firstName} ${reviewer.lastName}",
    stars = score,
    comment = comment,
    // Evitar crash si createdAt es más corto
    date = createdAt.takeIf { it.length >= 10 }?.substring(0, 10).orEmpty()
)
