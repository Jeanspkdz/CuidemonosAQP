package com.jean.cuidemonosaqp.shared.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.jean.cuidemonosaqp.BuildConfig
import com.jean.cuidemonosaqp.shared.network.AuthInterceptor
import com.jean.cuidemonosaqp.shared.preferences.DATA_STORE_FILE_NAME
import com.jean.cuidemonosaqp.shared.preferences.Session
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import com.jean.cuidemonosaqp.shared.preferences.SessionRepositoryImp
import com.jean.cuidemonosaqp.shared.preferences.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Session> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) }
        )
    }

    @Provides @Singleton
    fun provideSessionRepoImp(dataStore: DataStore<Session>): SessionRepository =
        SessionRepositoryImp(dataStore)

    @Provides @Singleton
    fun provideAuthInterceptor(sessionRepository: SessionRepository): AuthInterceptor =
        AuthInterceptor(sessionRepository)

    @Provides @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }
    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://192.168.51.117:3000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
