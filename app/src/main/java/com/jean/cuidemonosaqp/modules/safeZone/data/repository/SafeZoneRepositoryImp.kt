package com.jean.cuidemonosaqp.modules.safeZone.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.data.remote.SafeZoneApi
import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject
import com.jean.cuidemonosaqp.modules.safeZone.data.model.SafeZoneResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
            NetworkResult.Error(e.message ?: "Algo salio mal")
        }
    }

    override suspend fun getSafeZoneById(id:String): NetworkResult<SafeZoneResponseDTO> {
        return try {
            val response = safeZoneApi.getSafeZoneById(id)
            val bodyResponse =  response.body()
            if(response.isSuccessful && bodyResponse !== null){
                NetworkResult.Success(bodyResponse)
            }else {
                val errCode = response.code()
                val errMessage = when(errCode){
                    404 -> "Zona no Encontrada"
                    500 -> "Error al Encontrar la Zona"
                    else -> "Error Inesperado"
                }
                NetworkResult.Error(errMessage)
            }

        }catch (e: Exception){
            Log.e("PointRepositoryImp", "Exception GetZoneById: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Algo salio mal")
        }
    }

    override suspend fun createSafeZoneWithImage(
        fields: Map<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ): SafeZoneResponse {
        return safeZoneApi.createSafeZoneWithImage(
            name = fields["name"]!!,
            description = fields["description"],
            category = fields["category"],
            justification = fields["justification"]!!,
            assumesResponsibility = fields["assumes_responsibility"]!!,
            latitude = fields["latitude"]!!,
            longitude = fields["longitude"]!!,
            statusId = fields["status_id"]!!,
            userIds = fields["user_ids"]!!,
            rating = fields["rating"]!!,
            photo_url = photoPart
        )
    }
}