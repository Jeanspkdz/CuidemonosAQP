package com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.GetSafeZoneByIdUseCase
import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SafeZoneDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSafeZoneByIdUseCase: GetSafeZoneByIdUseCase
): ViewModel() {
    private val _safeZone = MutableStateFlow<SafeZoneResponseDTO?>(null)
    val safeZone = _safeZone
        .onStart {
            getSafeZoneDetail()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _errMessage = MutableStateFlow("")
    val errMessage = _errMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private fun getSafeZoneDetail(){
        viewModelScope.launch {
            val route = savedStateHandle.toRoute<Routes.SafeZone.Detail>()
            val id = route.id

            _isLoading.value = true

            when(val result = getSafeZoneByIdUseCase(id)){
                is NetworkResult.Error -> {
                    _errMessage.value = result.message
                }
                is NetworkResult.Success -> {
                    _safeZone.value = result.data
                }
                NetworkResult.Loading -> {}
            }

            _isLoading.value = false
        }

    }

    companion object {
        const val  TAG = "SafeZoneDetailViewModel"
    }

    init {
        Log.d(TAG, savedStateHandle.toRoute<Routes.SafeZone.Detail>().id)
    }
}