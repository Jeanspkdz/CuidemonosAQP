package com.jean.cuidemonosaqp.modules.user.data.mapper

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponse
import com.jean.cuidemonosaqp.modules.user.domain.model.User


fun UserResponse.toDomain(): User = User(
    id = id,
    dni = dni,
    dniExtension = dniExtension,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address,
    dniPhotoUrl = dniPhotoUrl,
    profilePhotoUrl = profilePhotoUrl,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt
)