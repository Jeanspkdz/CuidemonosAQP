package com.jean.cuidemonosaqp.modules.notification.data.dto

import com.google.gson.annotations.SerializedName

data class UserSafeZoneInvitationUpdateDto(
    @SerializedName("is_seen")
    val isSeen: Boolean,

    @SerializedName("confirmed_at")
    val confirmedAt: String?,

    val status: String
)