package com.jean.cuidemonosaqp.modules.notification.domain.usecase

import com.jean.cuidemonosaqp.modules.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class GetUserSafeZoneInvitations @Inject constructor(
   private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(userId:String) = notificationRepository.getUserSafeZoneInvitations(userId)
}