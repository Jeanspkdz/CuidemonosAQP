package com.jean.cuidemonosaqp.modules.safeZone.data.model

data class SafeZoneResponse(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val justification: String,
    val photoUrl: String,
    val rating: Double,
    val latitude: Double,
    val longitude: Double,
    val statusId: Int,
    val createdAt: String
)
