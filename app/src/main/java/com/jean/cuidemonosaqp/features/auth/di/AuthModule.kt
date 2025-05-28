package com.jean.cuidemonosaqp.features.auth.di

import com.jean.cuidemonosaqp.features.auth.data.AuthRepositoryImpl
import com.jean.cuidemonosaqp.features.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}