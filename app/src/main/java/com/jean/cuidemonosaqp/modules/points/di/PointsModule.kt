package com.jean.cuidemonosaqp.modules.points.di

import com.jean.cuidemonosaqp.modules.points.data.remote.PointApi
import com.jean.cuidemonosaqp.modules.points.data.repository.PointRepositoryImp
import com.jean.cuidemonosaqp.modules.points.domain.repository.PointRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PointsModule {

    @Provides
    @Singleton
    fun providePointsApi(retrofit: Retrofit): PointApi {
        return retrofit.create(PointApi::class.java)
    }

    @Provides
    @Singleton
    fun providePointRepository(pointApi: PointApi): PointRepository {
        return PointRepositoryImp(pointApi)
    }
}