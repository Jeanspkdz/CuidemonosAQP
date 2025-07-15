package com.jean.cuidemonosaqp.shared.network

import com.jean.cuidemonosaqp.shared.preferences.SessionCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionCache: SessionCache
) : Interceptor {

    @Volatile
    private var currentToken: String? = null

    init {
        // 1) Carga inicial bloqueante para tener token al arrancar
        runBlocking {
            currentToken = sessionCache.getToken()
        }
        // 2) Luego actualizamos en caliente cuando cambie
        CoroutineScope(Dispatchers.IO).launch {
            sessionCache.observeToken().collectLatest { token ->
                currentToken = token
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().also {
            currentToken?.let { t ->
                it.addHeader("Authorization", "Bearer $t")
            }
        }.build()
        return chain.proceed(request)
    }
}
