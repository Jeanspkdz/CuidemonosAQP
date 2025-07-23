package com.jean.cuidemonosaqp.modules.notification.domain.usecase

import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationUpdateDto
import com.jean.cuidemonosaqp.modules.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateUserSafeZoneInvitation @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(invitationId: String,  invitation: UserSafeZoneInvitationUpdateDto) =
        notificationRepository.updateUserSafeZoneInvitation(invitationId= invitationId,invitation)
}