package com.jean.cuidemonosaqp.modules.notification.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationResponseDto
import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationUpdateDto
import com.jean.cuidemonosaqp.modules.notification.data.remote.UserSafeZoneInvitationApi
import com.jean.cuidemonosaqp.modules.notification.domain.repository.NotificationRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val userSafeZoneInvitationApi: UserSafeZoneInvitationApi
) : NotificationRepository {
    companion object {
        const val TAG = "NotificationRepImp"
    }

    override suspend fun getUserSafeZoneInvitations(userId: String): NetworkResult<List<UserSafeZoneInvitationResponseDto>> {
        return try {
            val response = userSafeZoneInvitationApi.getUserSafeZoneInvitations(userId)

            if (response.isSuccessful && response.body() != null) {
                val invitations = response.body()!!
                Log.d(TAG, "Successful: $invitations")
                NetworkResult.Success(invitations)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Token inválido"
                    403 -> "Permisos insuficientes"
                    404 -> "No se encontraron invitaciones"
                    500 -> "Error interno del servidor"
                    else -> "Algo salió mal"
                }
                Log.d(TAG, "NotSuccessful: ${response.code()}")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            NetworkResult.Error("Error de red o inesperado")
        }
    }

    override suspend fun updateUserSafeZoneInvitation(
        invitationId: String,
        modifiedInvitation: UserSafeZoneInvitationUpdateDto
    ): NetworkResult<UserSafeZoneInvitationResponseDto> {
        return try {
            val response = userSafeZoneInvitationApi.updateUserSafeZoneInvitation(
                invitation = modifiedInvitation,
                id = invitationId
            )

            if (response.isSuccessful && response.body() != null) {
                val updatedInvitation = response.body()!!
                Log.d(TAG, "Successful: $updatedInvitation")
                NetworkResult.Success(updatedInvitation)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Token inválido"
                    403 -> "Permisos insuficientes"
                    404 -> "No se encontraron invitaciones"
                    500 -> "Error interno del servidor"
                    else -> "Algo salió mal"
                }
                Log.d(TAG, "NotSuccessful: ${response.code()}")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            NetworkResult.Error("Error de red o inesperado")
        }
    }
}