package com.jean.cuidemonosaqp.modules.notification.ui

import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationResponseDto

fun UserSafeZoneInvitationResponseDto.toUI(): SafeZoneInvitationUI {
    return SafeZoneInvitationUI(
        id = id.toString(),
        inviterName = "${user.lastName} ${user.lastName}",
        safeZoneName = safeZone.name,
        safeZoneDescription = safeZone.description,
        confirmedAt = confirmedAt,
        isSeen = isSeen,
        status = SafeZoneInvitationStatus.valueOf(status)
    )
}