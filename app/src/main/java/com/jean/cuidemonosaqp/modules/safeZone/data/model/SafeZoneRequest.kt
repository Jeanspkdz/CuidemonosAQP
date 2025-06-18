package com.jean.cuidemonosaqp.modules.safeZone.data.model

data class SafeZoneRequest(
    val name: String,
    val description: String,
    val category: String,
    val justification: String,
    val assumesResponsibility: Boolean,
    val rating: Double,
    val latitude: Double,
    val longitude: Double,
    val statusId: Int,
    val userIds: List<Int>
)
