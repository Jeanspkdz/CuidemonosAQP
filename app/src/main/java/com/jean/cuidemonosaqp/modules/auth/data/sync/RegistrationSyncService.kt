// modules/auth/data/sync/RegistrationSyncService.kt
package com.jean.cuidemonosaqp.modules.auth.data.sync

import android.content.Context
import android.util.Log
import com.jean.cuidemonosaqp.modules.auth.data.local.dao.PendingRegistrationDao
import com.jean.cuidemonosaqp.modules.auth.data.local.entity.PendingRegistrationEntity
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.utils.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationSyncService @Inject constructor(
    private val pendingRegistrationDao: PendingRegistrationDao,
    private val authApi: AuthApi,
    private val context: Context
) {

    private val maxSyncAttempts = 3

    /**
     * Sincroniza todos los registros pendientes
     */
    suspend fun syncPendingRegistrations(): SyncResult = withContext(Dispatchers.IO) {
        try {
            val pendingRegistrations = pendingRegistrationDao.getPendingRegistrations()

            if (pendingRegistrations.isEmpty()) {
                return@withContext SyncResult.Success(0)
            }

            var syncedCount = 0
            var failedCount = 0
            val errors = mutableListOf<String>()

            for (registration in pendingRegistrations) {
                // Verificar límite de intentos
                if (registration.syncAttempts >= maxSyncAttempts) {
                    Log.w("SyncService", "Registro ${registration.id} ha excedido el límite de intentos")
                    continue
                }

                try {
                    val result = syncSingleRegistration(registration)
                    if (result) {
                        syncedCount++
                        // Limpiar archivos locales después de sincronización exitosa
                        cleanupLocalFiles(registration)
                    } else {
                        failedCount++
                    }
                } catch (e: Exception) {
                    Log.e("SyncService", "Error sincronizando registro ${registration.id}: ${e.message}")
                    failedCount++
                    errors.add("Registro ${registration.id}: ${e.message}")

                    // Actualizar intentos fallidos
                    updateFailedSyncAttempt(registration, e.message ?: "Error desconocido")
                }
            }

            // Limpiar registros sincronizados exitosamente
            pendingRegistrationDao.deleteSyncedRegistrations()

            when {
                failedCount == 0 -> SyncResult.Success(syncedCount)
                syncedCount == 0 -> SyncResult.Error("Falló la sincronización de todos los registros", errors)
                else -> SyncResult.PartialSuccess(syncedCount, failedCount, errors)
            }

        } catch (e: Exception) {
            Log.e("SyncService", "Error general en sincronización: ${e.message}")
            SyncResult.Error("Error general: ${e.message}", listOf())
        }
    }

    /**
     * Sincroniza un registro individual
     */
    private suspend fun syncSingleRegistration(registration: PendingRegistrationEntity): Boolean {
        return try {
            // Preparar datos del formulario
            val dniBody = registration.dni.toRequestBody("text/plain".toMediaTypeOrNull())
            val firstNameBody = registration.firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = registration.lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordBody = registration.password.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneBody = registration.phone.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = registration.email.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressBody = registration.address.toRequestBody("text/plain".toMediaTypeOrNull())
            val repStatusBody = registration.reputationStatusId.toRequestBody("text/plain".toMediaTypeOrNull())
            val dniExtBody = registration.dniExtension?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Preparar archivos de imagen
            val dniPart = registration.dniPhotoPath?.let { path ->
                createMultipartFromFile("dni_photo", path)
            }
            val profilePart = registration.profilePhotoPath?.let { path ->
                createMultipartFromFile("profile_photo", path)
            }

            // Realizar llamada al API
            val response = authApi.register(
                dni = dniBody,
                firstName = firstNameBody,
                lastName = lastNameBody,
                dniExtension = dniExtBody,
                password = passwordBody,
                phone = phoneBody,
                email = emailBody,
                address = addressBody,
                reputationStatusId = repStatusBody,
                dniPhoto = dniPart,
                profilePhoto = profilePart
            )

            if (response.isSuccessful) {
                // Marcar como sincronizado
                pendingRegistrationDao.markAsSynced(registration.id)
                Log.d("SyncService", "Registro ${registration.id} sincronizado exitosamente")
                true
            } else {
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()}"
                updateFailedSyncAttempt(registration, errorMsg)
                Log.e("SyncService", "Error sincronizando registro ${registration.id}: $errorMsg")
                false
            }

        } catch (e: Exception) {
            updateFailedSyncAttempt(registration, e.message ?: "Error desconocido")
            Log.e("SyncService", "Excepción sincronizando registro ${registration.id}: ${e.message}")
            false
        }
    }

    /**
     * Actualiza el registro con información del intento fallido
     */
    private suspend fun updateFailedSyncAttempt(registration: PendingRegistrationEntity, error: String) {
        pendingRegistrationDao.updateSyncAttempt(
            id = registration.id,
            attempts = registration.syncAttempts + 1,
            timestamp = System.currentTimeMillis(),
            error = error
        )
    }

    /**
     * Crea MultipartBody.Part desde archivo local
     */
    private fun createMultipartFromFile(fieldName: String, filePath: String): MultipartBody.Part? {
        return try {
            val file = FileManager.getFile(filePath) ?: return null
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(fieldName, file.name, requestBody)
        } catch (e: Exception) {
            Log.e("SyncService", "Error creando multipart desde archivo: ${e.message}")
            null
        }
    }

    /**
     * Limpia archivos locales después de sincronización exitosa
     */
    private fun cleanupLocalFiles(registration: PendingRegistrationEntity) {
        registration.dniPhotoPath?.let { FileManager.deleteFile(it) }
        registration.profilePhotoPath?.let { FileManager.deleteFile(it) }
    }

    /**
     * Obtener estadísticas de sincronización
     */
    suspend fun getSyncStats(): SyncStats = withContext(Dispatchers.IO) {
        val pendingCount = pendingRegistrationDao.getPendingCount()
        SyncStats(pendingCount = pendingCount)
    }
}

/**
 * Resultado de la sincronización
 */
sealed class SyncResult {
    data class Success(val syncedCount: Int) : SyncResult()
    data class PartialSuccess(val syncedCount: Int, val failedCount: Int, val errors: List<String>) : SyncResult()
    data class Error(val message: String, val errors: List<String>) : SyncResult()
}

/**
 * Estadísticas de sincronización
 */
data class SyncStats(
    val pendingCount: Int
)