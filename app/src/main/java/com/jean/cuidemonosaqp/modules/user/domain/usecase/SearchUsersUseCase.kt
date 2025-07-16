package com.jean.cuidemonosaqp.modules.user.domain.usecase

import com.jean.cuidemonosaqp.modules.user.domain.repository.UserRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(query: String): NetworkResult<List<User>> =
        repo.searchUsers(query)
}
