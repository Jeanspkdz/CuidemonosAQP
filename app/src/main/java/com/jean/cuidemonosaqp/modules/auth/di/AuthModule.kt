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
    fun provideAuthRepository(api: AuthApi, tokenManager: TokenManager): AuthRepository {
        return AuthRepositoryImpl(api, tokenManager)
    }
}
