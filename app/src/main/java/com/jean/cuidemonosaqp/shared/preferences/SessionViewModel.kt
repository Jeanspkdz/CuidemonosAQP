package com.jean.cuidemonosaqp.shared.preferences

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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