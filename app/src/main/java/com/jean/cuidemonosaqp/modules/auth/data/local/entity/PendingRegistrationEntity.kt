package com.jean.cuidemonosaqp.modules.auth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_registrations")
data class PendingRegistrationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dni: String,
    val firstName: String,
    val lastName: String,
    val dniExtension: String? = null,
    val password: String,
    val phone: String,
    val email: String,
    val address: String,
    val reputationStatusId: String,
    val profilePhotoPath: String? = null, // Ruta local del archivo
    val dniPhotoPath: String? = null, // Ruta local del archivo
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val syncAttempts: Int = 0,
    val lastSyncAttempt: Long? = null,
    val syncError: String? = null
)