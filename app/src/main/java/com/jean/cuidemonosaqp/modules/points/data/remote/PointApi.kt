package com.jean.cuidemonosaqp.modules.points.data.remote

import com.jean.cuidemonosaqp.modules.points.data.model.PointResponse
import retrofit2.Response
import retrofit2.http.GET

interface PointApi {
    @GET("/safezones")
    suspend fun getAllPoints(): Response<List<PointResponse>>
}