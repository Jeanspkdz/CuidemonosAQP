package com.jean.cuidemonosaqp.modules.safeZone.ui.list

data class SafeZoneListItem(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val rating: Double,
    val status: String,
    val isUserZone: Boolean // True if the current user is associated with this zone
)

data class SafeZoneListState(
    val safeZones: List<SafeZoneListItem> = emptyList(),
    val filteredSafeZones: List<SafeZoneListItem> = emptyList(),
    val searchQuery: String = "",
    val isShowingUserZones: Boolean = false, // Filter for "mis puntos"
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
) {
    companion object {
        const val PAGE_SIZE = 10
    }
}
