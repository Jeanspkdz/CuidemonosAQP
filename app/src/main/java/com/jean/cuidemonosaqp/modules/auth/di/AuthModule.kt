package com.jean.cuidemonosaqp.modules.auth.di

import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.modules.auth.data.repository.AuthRepositoryImpl
import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import android.content.Context
import com.jean.cuidemonosaqp.modules.auth.data.local.dao.PendingRegistrationDao
import com.jean.cuidemonosaqp.modules.auth.data.sync.RegistrationSyncService
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        tokenManager: TokenManager,
        pendingRegistrationDao: PendingRegistrationDao,
        syncService: RegistrationSyncService,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApi = api,
            tokenManager = tokenManager,
            pendingRegistrationDao = pendingRegistrationDao,
            syncService = syncService,
            context = context
        )
    }
}