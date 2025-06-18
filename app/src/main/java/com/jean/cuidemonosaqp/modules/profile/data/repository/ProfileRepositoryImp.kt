package com.jean.cuidemonosaqp.modules.profile.data.repository

import android.util.Log
import com.jean.cuidemonosaqp.modules.profile.data.model.UserInfoResponse
import com.jean.cuidemonosaqp.modules.profile.data.remote.ProfileApi
import com.jean.cuidemonosaqp.modules.profile.domain.repository.ProfileRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import javax.inject.Inject

class ProfileRepositoryImp @Inject constructor(
    private val profileApi : ProfileApi,
    private val tokenManager: TokenManager
) : ProfileRepository {

    override suspend fun getUserInfo() : NetworkResult<UserInfoResponse> {
        return try {
            val response = profileApi.getUserInfo()
            if (response.isSuccessful && response.body() != null) {
                Log.d("ProfileRepImp", "Response: ${response.body()!!} ")
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(response.errorBody()?.string() ?: "Login failed")
            }
        }catch (e: Exception){
            NetworkResult.Error(e.message ?: "Something went wrong")
        }
    }
}