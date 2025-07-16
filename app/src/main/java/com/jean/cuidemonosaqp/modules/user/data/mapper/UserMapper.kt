package com.jean.cuidemonosaqp.modules.user.data.mapper

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import com.jean.cuidemonosaqp.modules.user.domain.model.User

fun UserResponseDto.toDomain(): User = User(
    id               = id,
    dni              = dni,
    dniExtension     = dniExtension,     // mapea null a null
    firstName        = firstName,
    lastName         = lastName,
    phone            = phone,
    email            = email,
    address          = address,
    dniPhotoUrl      = dniPhotoUrl,
    profilePhotoUrl  = profilePhotoUrl,
    isActive         = isActive,
    createdAt        = createdAt,
    updatedAt        = updatedAt
)