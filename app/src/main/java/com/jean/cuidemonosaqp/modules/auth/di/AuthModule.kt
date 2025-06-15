package com.jean.cuidemonosaqp.modules.auth.di

import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthRemoteDatasource
import com.jean.cuidemonosaqp.modules.auth.domain.AuthRepository
import com.jean.cuidemonosaqp.modules.auth.data.repository.AuthRepositoryImp
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthRetrofitDatasource
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDatasource(authService: AuthService): AuthRemoteDatasource {
        return AuthRetrofitDatasource(authService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authRemoteDatasource: AuthRemoteDatasource): AuthRepository {
        return AuthRepositoryImp(authRemoteDatasource = authRemoteDatasource)
    }
}

