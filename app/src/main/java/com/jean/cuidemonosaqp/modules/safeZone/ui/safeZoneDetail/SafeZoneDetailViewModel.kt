package com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.jean.cuidemonosaqp.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SafeZoneDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    companion object {
        const val  TAG = "SafeZoneDetailViewModel"
    }

    init {
//        Log.d(TAG, savedStateHandle.toRoute<Routes.SafeZone.Detail>().id)
    }
}