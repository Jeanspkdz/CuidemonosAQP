package com.jean.cuidemonosaqp.shared.network

import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionCache: SessionRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().also {
            val token = sessionCache.getTokenSync()
            token?.let { t ->
                it.addHeader("Authorization", "Bearer $t")
            }
        }.build()
        return chain.proceed(request)
    }
}
