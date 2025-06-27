package com.jean.cuidemonosaqp.modules.userreviews.di

import com.jean.cuidemonosaqp.modules.userreviews.data.remote.UserReviewApi
import com.jean.cuidemonosaqp.modules.userreviews.data.repository.UserReviewRepositoryImp
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserReviewModule {

    @Provides
    @Singleton
    fun provideUserReviewApi(retrofit: Retrofit) : UserReviewApi {
        return retrofit.create(UserReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserReviewRepository(userReviewApi: UserReviewApi): UserReviewRepository {
        return UserReviewRepositoryImp(userReviewApi)
    }
}