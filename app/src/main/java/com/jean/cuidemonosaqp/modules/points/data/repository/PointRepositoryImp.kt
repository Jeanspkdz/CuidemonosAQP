package com.jean.cuidemonosaqp.modules.points.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.points.data.model.PointResponse
import com.jean.cuidemonosaqp.modules.points.data.remote.PointApi
import com.jean.cuidemonosaqp.modules.points.domain.repository.PointRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class PointRepositoryImp @Inject constructor(
    private val pointApi: PointApi
) : PointRepository {
    override suspend fun getAllPoints(): NetworkResult<List<PointResponse>> {
        return try {
            val response = pointApi.getAllPoints()
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