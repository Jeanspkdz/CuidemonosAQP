package com.jean.cuidemonosaqp.modules.safeZone.ui.list

import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO

fun SafeZoneResponseDTO.toListItem(currentUserId: String): SafeZoneListItem {
    // Check if current user is associated with this safe zone
    val isUserZone = users.any { user -> user.id.toString() == currentUserId }
    
    // Calculate average rating if available (for now using a placeholder)
    val calculatedRating = (rating ?: 0.0).toDouble()
    
    return SafeZoneListItem(
        id = id,
        name = name,
        photoUrl = photoUrl,
        rating = calculatedRating,
        status = status?.description ?: "Estado desconocido",
        isUserZone = isUserZone
    )
}
