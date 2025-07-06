package com.jean.cuidemonosaqp.modules.safeZone.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.data.remote.SafeZoneApi
import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class SafeZoneRepositoryImp @Inject constructor(
    private val safeZoneApi: SafeZoneApi
) : SafeZoneRepository {
    override suspend fun getAllSafeZones(): NetworkResult<List<SafeZoneResponseDTO>> {
        return try {
            val response = safeZoneApi.getAllSafeZones()
            if (response.isSuccessful && response.body() != null) {
                Log.d("PointRepositoryImp", "Response: ${response.body()}")
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido al obtener puntos"
                Log.e("PointRepositoryImp", "Error: $errorMsg")
                NetworkResult.Error(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("PointRepositoryImp", "Exception: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Excepción desconocida al obtener puntos")
        }
    }
}