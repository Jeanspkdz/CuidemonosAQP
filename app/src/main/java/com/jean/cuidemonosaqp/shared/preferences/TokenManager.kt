package com.jean.cuidemonosaqp.shared.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun getAccessTokenSync(): String? = getAccessToken()

    fun clearToken() {
        prefs.edit().remove("access_token").apply()
    }
}