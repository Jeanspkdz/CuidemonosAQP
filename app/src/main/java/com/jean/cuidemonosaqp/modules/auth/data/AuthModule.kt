package com.jean.cuidemonosaqp.modules.auth.data

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
    private const val URL = "https://cuidemonosaqp-backend.onrender.com"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDatasource(authService: AuthService):AuthRemoteDatasource{
        return AuthRetrofitDatasource(authService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authRemoteDatasource: AuthRemoteDatasource): AuthRepository{
        return AuthRepositoryImp(authRemoteDatasource = authRemoteDatasource)
    }
}

