package com.jean.cuidemonosaqp.modules.safeZone.di

import com.jean.cuidemonosaqp.modules.safeZone.data.remote.SafeZoneApi
import com.jean.cuidemonosaqp.modules.safeZone.data.repository.SafeZoneRepositoryImp
import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SafeZoneModule {

    @Provides
    @Singleton
    fun provideSafeZoneApi(retrofit: Retrofit): SafeZoneApi {
        return retrofit.create(SafeZoneApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSafeZoneRepository(pointApi: SafeZoneApi): SafeZoneRepository {
        return SafeZoneRepositoryImp(pointApi)
    }
}