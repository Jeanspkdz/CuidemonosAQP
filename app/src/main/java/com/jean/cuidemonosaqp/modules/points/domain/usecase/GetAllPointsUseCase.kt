package com.jean.cuidemonosaqp.modules.points.domain.usecase

import com.jean.cuidemonosaqp.modules.points.data.model.PointResponse
import com.jean.cuidemonosaqp.modules.points.domain.repository.PointRepository
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import javax.inject.Inject

class GetAllPointsUseCase @Inject constructor(
    private val repository: PointRepository
) {
    suspend operator fun invoke(): NetworkResult<List<PointResponse>> {
        return repository.getAllPoints()
    }
}