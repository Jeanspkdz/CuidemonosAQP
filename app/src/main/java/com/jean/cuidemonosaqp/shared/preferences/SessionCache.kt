package com.jean.cuidemonosaqp.shared.preferences

import kotlinx.coroutines.flow.Flow

interface SessionCache {
    suspend fun updateSession(session: Session)
    suspend fun getToken(): String?
    suspend fun getUserId(): String?
    fun observeUserId(): Flow<String?>
    fun observeToken(): Flow<String?>
    suspend fun updateToken(token: String?)
    suspend fun updateUserId(id: String?)
    suspend fun clearSession()
}