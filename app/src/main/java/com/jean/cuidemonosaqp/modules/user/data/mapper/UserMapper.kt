package com.jean.cuidemonosaqp.modules.user.data.mapper

import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import com.jean.cuidemonosaqp.modules.user.domain.model.User

fun UserResponseDto.toDomain(): User = User(
    id = id,
    dni = dni,
    dniExtension = dniExtension,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    email = email,
    address = address,
    addressLatitude = addressLatitude,
    addressLongitude = addressLongitude,
    dniPhotoUrl = dniPhotoUrl,
    profilePhotoUrl = profilePhotoUrl,
    reputationScore = reputationScore,
    reputationStatusId = reputationStatusId,
    isActive = isActive,
    refreshToken = refreshToken,
    createdAt = createdAt,
    updatedAt = updatedAt
)