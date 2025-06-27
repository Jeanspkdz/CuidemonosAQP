package com.jean.cuidemonosaqp.modules.user.di

import com.jean.cuidemonosaqp.modules.user.data.repository.UserRepositoryImp
import com.jean.cuidemonosaqp.modules.user.data.remote.UserApi
import com.jean.cuidemonosaqp.modules.user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule{

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi) : UserRepository {
        return UserRepositoryImp(userApi)
    }
}