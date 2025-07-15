package com.jean.cuidemonosaqp.shared.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.jean.cuidemonosaqp.BuildConfig
import com.jean.cuidemonosaqp.shared.network.AuthInterceptor
import com.jean.cuidemonosaqp.shared.preferences.DATA_STORE_FILE_NAME
import com.jean.cuidemonosaqp.shared.preferences.Session
import com.jean.cuidemonosaqp.shared.preferences.SessionCache
import com.jean.cuidemonosaqp.shared.preferences.SessionCacheImp
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import com.jean.cuidemonosaqp.shared.preferences.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Session> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun providesSessionCacheImp(dataStore: DataStore<Session>): SessionCache {
        return SessionCacheImp(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionCache: SessionCache): AuthInterceptor =
        AuthInterceptor(sessionCache)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(
                "https://cuidemonosaqp-backend.onrender.com"
                //"http://192.168.41.117:3000/"
            )
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}