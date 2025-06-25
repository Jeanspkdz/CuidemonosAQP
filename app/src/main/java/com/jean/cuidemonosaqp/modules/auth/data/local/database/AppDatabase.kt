package com.jean.cuidemonosaqp.modules.auth.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.jean.cuidemonosaqp.modules.auth.data.local.dao.PendingRegistrationDao
import com.jean.cuidemonosaqp.modules.auth.data.local.entity.PendingRegistrationEntity

@Database(
    entities = [PendingRegistrationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pendingRegistrationDao(): PendingRegistrationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cuidemonos_aqp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}