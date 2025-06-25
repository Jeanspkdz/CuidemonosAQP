package com.jean.cuidemonosaqp.modules.auth.data.local.dao

import androidx.room.*
import com.jean.cuidemonosaqp.modules.auth.data.local.entity.PendingRegistrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingRegistrationDao {

    @Insert
    suspend fun insertPendingRegistration(registration: PendingRegistrationEntity): Long

    @Query("SELECT * FROM pending_registrations WHERE isSynced = 0")
    suspend fun getPendingRegistrations(): List<PendingRegistrationEntity>

    @Update
    suspend fun updatePendingRegistration(registration: PendingRegistrationEntity): Int

    @Query("UPDATE pending_registrations SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long): Int

    @Query("""
        UPDATE pending_registrations 
        SET syncAttempts = :attempts, 
            lastSyncAttempt = :timestamp, 
            syncError = :error 
        WHERE id = :id
    """)
    suspend fun updateSyncAttempt(
        id: Long,
        attempts: Int,
        timestamp: Long,
        error: String?
    ): Int

    @Delete
    suspend fun deletePendingRegistration(registration: PendingRegistrationEntity): Int

    @Query("DELETE FROM pending_registrations WHERE isSynced = 1")
    suspend fun deleteSyncedRegistrations(): Int

    @Query("SELECT COUNT(*) FROM pending_registrations WHERE isSynced = 0")
    suspend fun getPendingCount(): Int

    @Query("SELECT COUNT(*) FROM pending_registrations WHERE isSynced = 0")
    fun getPendingCountFlow(): Flow<Int>
}