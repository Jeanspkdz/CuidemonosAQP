package com.jean.cuidemonosaqp.modules.safeZone.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.GetAllSafeZonesUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SafeZoneListViewModel @Inject constructor(
    private val getAllSafeZonesUseCase: GetAllSafeZonesUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SafeZoneListVM"
    }

    private val _state = MutableStateFlow(SafeZoneListState())
    val state: StateFlow<SafeZoneListState> = _state.asStateFlow()

    private var allSafeZones: List<SafeZoneListItem> = emptyList()

    init {
        loadSafeZones()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { currentState ->
            currentState.copy(searchQuery = query)
        }
        applyFilters()
    }

    fun onToggleUserZonesFilter() {
        _state.update { currentState ->
            currentState.copy(isShowingUserZones = !currentState.isShowingUserZones)
        }
        applyFilters()
    }

    fun onLoadMore() {
        val currentState = _state.value
        if (!currentState.hasMorePages || currentState.isLoading) return

        val nextPage = currentState.currentPage + 1
        val startIndex = nextPage * SafeZoneListState.PAGE_SIZE
        val endIndex = minOf(startIndex + SafeZoneListState.PAGE_SIZE, currentState.filteredSafeZones.size)

        if (startIndex < currentState.filteredSafeZones.size) {
            _state.update { state ->
                state.copy(
                    currentPage = nextPage,
                    hasMorePages = endIndex < currentState.filteredSafeZones.size
                )
            }
        }
    }

    fun refresh() {
        loadSafeZones()
    }

    private fun loadSafeZones() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = getAllSafeZonesUseCase()) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Loaded ${result.data.size} safe zones")
                    val currentUserId = sessionRepository.getUserId() ?: ""
                    
                    allSafeZones = result.data.map { safeZone ->
                        safeZone.toListItem(currentUserId)
                    }

                    _state.update { currentState ->
                        currentState.copy(
                            safeZones = allSafeZones,
                            isLoading = false,
                            error = null,
                            currentPage = 0,
                            hasMorePages = allSafeZones.size > SafeZoneListState.PAGE_SIZE
                        )
                    }
                    applyFilters()
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "Error loading safe zones: ${result.message}")
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            error = result.message ?: "Error al cargar zonas seguras"
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    private fun applyFilters() {
        val currentState = _state.value
        var filtered = allSafeZones

        // Apply user zones filter
        if (currentState.isShowingUserZones) {
            filtered = filtered.filter { it.isUserZone }
        }

        // Apply search filter
        if (currentState.searchQuery.isNotBlank()) {
            val query = currentState.searchQuery.lowercase().trim()
            filtered = filtered.filter { safeZone ->
                safeZone.name.lowercase().contains(query) ||
                safeZone.status.lowercase().contains(query)
            }
        }

        _state.update { state ->
            state.copy(
                filteredSafeZones = filtered,
                currentPage = 0,
                hasMorePages = filtered.size > SafeZoneListState.PAGE_SIZE
            )
        }
    }

    fun getCurrentPageItems(): List<SafeZoneListItem> {
        val currentState = _state.value
        val startIndex = 0
        val endIndex = minOf(
            (currentState.currentPage + 1) * SafeZoneListState.PAGE_SIZE,
            currentState.filteredSafeZones.size
        )
        return currentState.filteredSafeZones.take(endIndex)
    }
}
