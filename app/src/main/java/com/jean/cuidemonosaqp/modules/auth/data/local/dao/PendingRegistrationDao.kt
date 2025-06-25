package com.jean.cuidemonosaqp.modules.auth.data.local.dao

import androidx.room.*
import com.jean.cuidemonosaqp.modules.auth.data.local.entity.PendingRegistrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingRegistrationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingRegistration(registration: PendingRegistrationEntity): Long

    @Query("SELECT * FROM pending_registrations WHERE isSynced = 0 ORDER BY createdAt ASC")
    suspend fun getPendingRegistrations(): List<PendingRegistrationEntity>

    @Query("SELECT * FROM pending_registrations WHERE isSynced = 0 ORDER BY createdAt ASC")
    fun getPendingRegistrationsFlow(): Flow<List<PendingRegistrationEntity>>

    @Update
    suspend fun updatePendingRegistration(registration: PendingRegistrationEntity)

    @Query("UPDATE pending_registrations SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long)

    @Query("UPDATE pending_registrations SET syncAttempts = :attempts, lastSyncAttempt = :timestamp, syncError = :error WHERE id = :id")
    suspend fun updateSyncAttempt(id: Long, attempts: Int, timestamp: Long, error: String?)

    @Delete
    suspend fun deletePendingRegistration(registration: PendingRegistrationEntity)

    @Query("DELETE FROM pending_registrations WHERE isSynced = 1")
    suspend fun deleteSyncedRegistrations()

    @Query("SELECT COUNT(*) FROM pending_registrations WHERE isSynced = 0")
    suspend fun getPendingCount(): Int

    @Query("SELECT COUNT(*) FROM pending_registrations WHERE isSynced = 0")
    fun getPendingCountFlow(): Flow<Int>
}