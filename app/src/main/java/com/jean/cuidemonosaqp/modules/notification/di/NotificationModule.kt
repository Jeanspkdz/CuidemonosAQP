package com.jean.cuidemonosaqp.modules.notification.di

import com.jean.cuidemonosaqp.modules.notification.data.remote.UserSafeZoneInvitationApi
import com.jean.cuidemonosaqp.modules.notification.data.repository.NotificationRepositoryImp
import com.jean.cuidemonosaqp.modules.notification.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(userSafeZoneInvitationApi: UserSafeZoneInvitationApi): NotificationRepository {
        return NotificationRepositoryImp(userSafeZoneInvitationApi)
    }

    @Provides
    @Singleton
    fun provideUserSafeZoneInvitationApi(retrofit: Retrofit): UserSafeZoneInvitationApi {
        return retrofit.create(UserSafeZoneInvitationApi::class.java)
    }
}