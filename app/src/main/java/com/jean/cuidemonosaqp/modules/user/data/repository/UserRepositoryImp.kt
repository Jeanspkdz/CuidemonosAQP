package com.jean.cuidemonosaqp.modules.user.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import com.jean.cuidemonosaqp.modules.user.data.mapper.toDomain
import com.jean.cuidemonosaqp.modules.user.data.remote.UserApi
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.user.domain.repository.UserRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    companion object {
        const val TAG = "UserRepoImpl"
    }

    override suspend fun getUserById(id: String): NetworkResult<User> {
        return try {
            val response = userApi.getUserById(id)
            val data: UserResponseDto? = response.body()

            if (response.isSuccessful && data !== null) {
                Log.d(TAG, "Successful : $data")
                NetworkResult.Success(data.toDomain())
            } else {
                val errorMessage = when(response.code()){
                    401 -> "Token Invalido"
                    403 -> "Permisos Necesarios"
                    500 -> "Error al obtener usuarios"
                    else -> "Algo salio mal"
                }

                Log.d(TAG, "NotSuccessful : ${response.code()}")
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.d(TAG, "${e.message}")
            NetworkResult.Error("Algo salio Mal")
        }
    }

    override suspend fun searchUsers(query: String): NetworkResult<List<User>> {
        Log.d(TAG, "→ searchUsers(query=\"$query\")")
        return try {
            val resp = userApi.searchUsers(query)
            val body = resp.body()
            val err  = resp.errorBody()?.string()
            Log.d(TAG, "← HTTP ${resp.code()} bodySize=${body?.size} err=$err")
            if (resp.isSuccessful && body != null) {
                val list = body.map { it.toDomain() }
                NetworkResult.Success(list)
            } else {
                NetworkResult.Error("HTTP ${resp.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception en searchUsers", e)
            NetworkResult.Error(e.message ?: "Error desconocido")
        }
    }
}