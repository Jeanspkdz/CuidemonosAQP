package com.jean.cuidemonosaqp.modules.notification.ui

enum class SafeZoneInvitationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}


data class SafeZoneInvitationUI(
    val id: String,
    val status:SafeZoneInvitationStatus,
    val inviterName: String,
    val safeZoneName: String,
    val safeZoneDescription: String,
    val confirmedAt: String?,
    val isSeen: Boolean
)
