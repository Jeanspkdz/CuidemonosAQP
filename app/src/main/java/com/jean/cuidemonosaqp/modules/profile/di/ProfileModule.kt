package com.jean.cuidemonosaqp.modules.profile.di

import com.jean.cuidemonosaqp.modules.profile.data.remote.ProfileApi
import com.jean.cuidemonosaqp.modules.profile.data.repository.ProfileRepositoryImp
import com.jean.cuidemonosaqp.modules.profile.domain.repository.ProfileRepository
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit) : ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(profileApi : ProfileApi, tokenManager: TokenManager): ProfileRepository {
        return ProfileRepositoryImp(profileApi , tokenManager)
    }
}