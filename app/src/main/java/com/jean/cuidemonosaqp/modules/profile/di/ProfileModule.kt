package com.jean.cuidemonosaqp.modules.profile.di

import com.jean.cuidemonosaqp.modules.profile.data.repository.ProfileRepositoryImp
import com.jean.cuidemonosaqp.modules.profile.domain.repository.ProfileRepository
import com.jean.cuidemonosaqp.modules.userreviews.domain.repository.UserReviewRepository
import com.jean.cuidemonosaqp.modules.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        userRepository: UserRepository,
        userReviewRepository: UserReviewRepository
    ): ProfileRepository {
        return ProfileRepositoryImp(
            userRepository = userRepository,
            userReviewRepository = userReviewRepository
        )
    }
}