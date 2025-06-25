package com.jean.cuidemonosaqp.shared.di

import android.content.Context
import androidx.room.Room
import com.jean.cuidemonosaqp.modules.auth.data.local.dao.PendingRegistrationDao
import com.jean.cuidemonosaqp.modules.auth.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cuidemonos_aqp_database"
        ).build()
    }

    @Provides
    fun providePendingRegistrationDao(appDatabase: AppDatabase): PendingRegistrationDao {
        return appDatabase.pendingRegistrationDao()
    }
}
