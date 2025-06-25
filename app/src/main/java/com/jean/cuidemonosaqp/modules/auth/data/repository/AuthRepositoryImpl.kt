package com.jean.cuidemonosaqp.modules.auth.data.repository

import com.jean.cuidemonosaqp.modules.auth.data.model.*
import com.jean.cuidemonosaqp.modules.auth.data.remote.AuthApi
import com.jean.cuidemonosaqp.modules.auth.domain.repository.AuthRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

import android.content.Context
import android.util.Log
import com.jean.cuidemonosaqp.modules.auth.data.local.dao.PendingRegistrationDao
import com.jean.cuidemonosaqp.modules.auth.data.local.entity.PendingRegistrationEntity
import com.jean.cuidemonosaqp.modules.auth.data.model.RegisterResponse
import com.jean.cuidemonosaqp.modules.auth.data.sync.RegistrationSyncService
import com.jean.cuidemonosaqp.modules.auth.data.sync.SyncResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val pendingRegistrationDao: PendingRegistrationDao,
    private val syncService: RegistrationSyncService,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override suspend fun login(emailOrDni: String, password: String): NetworkResult<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(emailOrDni, password))
            if (response.isSuccessful && response.body() != null) {
                tokenManager.saveAccessToken(response.body()!!.accessToken)
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Login failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun register(
        dni: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        dniExtension: RequestBody?,
        password: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        address: RequestBody,
        reputationStatusId: RequestBody,
        dniPhoto: MultipartBody.Part?,
        profilePhoto: MultipartBody.Part?
    ): NetworkResult<RegisterResponse> {
        return try {
            val response = authApi.register(
                dni,
                firstName,
                lastName,
                dniExtension,
                password,
                phone,
                email,
                address,
                reputationStatusId,
                dniPhoto,
                profilePhoto
            )
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Register failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.localizedMessage ?: "Ha ocurrido un error inesperado durante el registro.")
        }
    }

    override suspend fun saveRegistrationOffline(
        dni: String,
        firstName: String,
        lastName: String,
        dniExtension: String?,
        password: String,
        phone: String,
        email: String,
        address: String,
        reputationStatusId: String,
        profilePhotoPath: String?,
        dniPhotoPath: String?
    ): Long {
        val pendingRegistration = PendingRegistrationEntity(
            dni = dni,
            firstName = firstName,
            lastName = lastName,
            dniExtension = dniExtension,
            password = password,
            phone = phone,
            email = email,
            address = address,
            reputationStatusId = reputationStatusId,
            profilePhotoPath = profilePhotoPath,
            dniPhotoPath = dniPhotoPath
        )

        return pendingRegistrationDao.insertPendingRegistration(pendingRegistration)
    }

    override suspend fun getPendingRegistrationsCount(): Int {
        return pendingRegistrationDao.getPendingCount()
    }

    override fun getPendingRegistrationsCountFlow(): Flow<Int> {
        return pendingRegistrationDao.getPendingCountFlow()
    }

    override suspend fun syncPendingRegistrations(): Boolean {
        return try {
            when (val result = syncService.syncPendingRegistrations()) {
                is SyncResult.Success -> {
                    Log.d("AuthRepository", "Sincronización exitosa: ${result.syncedCount} registros")
                    true
                }
                is SyncResult.PartialSuccess -> {
                    Log.w("AuthRepository", "Sincronización parcial: ${result.syncedCount} exitosos, ${result.failedCount} fallidos")
                    true // Consideramos parcial como éxito
                }
                is SyncResult.Error -> {
                    Log.e("AuthRepository", "Error en sincronización: ${result.message}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Excepción en syncPendingRegistrations: ${e.message}")
            false
        }
    }
}