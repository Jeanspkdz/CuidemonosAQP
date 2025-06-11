package com.jean.cuidemonosaqp.modules.auth.data

import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val authRemoteDatasource: AuthRemoteDatasource,
    // localDatasource
) : AuthRepository{
    override suspend fun login(identifier: String, password: String) {
        authRemoteDatasource.login(identifier = identifier, password= password)
    }
}