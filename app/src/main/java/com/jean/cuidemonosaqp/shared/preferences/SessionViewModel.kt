package com.jean.cuidemonosaqp.shared.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionCache: SessionCache
) : ViewModel() {

    val userId: Flow<String?> = sessionCache.observeUserId()
    val token: Flow<String?> = sessionCache.observeToken()

    val isAuthenticated: Flow<Boolean> = combine(userId, token) { id, token ->
        !id.isNullOrEmpty() && !token.isNullOrEmpty()
    }
}