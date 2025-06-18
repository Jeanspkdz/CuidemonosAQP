package com.jean.cuidemonosaqp.modules.points.domain.repository

import com.jean.cuidemonosaqp.modules.points.data.model.PointResponse
import com.jean.cuidemonosaqp.shared.network.NetworkResult

interface PointRepository {
    suspend fun getAllPoints(): NetworkResult<List<PointResponse>>
}