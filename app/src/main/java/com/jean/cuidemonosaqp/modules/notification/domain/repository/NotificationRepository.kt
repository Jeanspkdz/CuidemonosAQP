package com.jean.cuidemonosaqp.modules.notification.domain.repository

import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationResponseDto
import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationUpdateDto
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface NotificationRepository {

    suspend fun getUserSafeZoneInvitations(userId: String): NetworkResult<List<UserSafeZoneInvitationResponseDto>>
    suspend fun updateUserSafeZoneInvitation(invitationId: String, modifiedInvitation:UserSafeZoneInvitationUpdateDto): NetworkResult<UserSafeZoneInvitationResponseDto>
}