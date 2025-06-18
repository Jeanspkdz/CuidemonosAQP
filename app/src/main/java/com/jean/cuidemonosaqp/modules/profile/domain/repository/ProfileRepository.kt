package com.jean.cuidemonosaqp.modules.profile.domain.repository

import com.jean.cuidemonosaqp.modules.profile.data.model.UserInfoResponse
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface ProfileRepository {

    suspend fun getUserInfo() : NetworkResult<UserInfoResponse>
}