package com.jean.cuidemonosaqp.modules.user.domain.repository

import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.modules.user.domain.model.User

interface UserRepository {

    suspend fun getUserById(id:String): NetworkResult<User>
}