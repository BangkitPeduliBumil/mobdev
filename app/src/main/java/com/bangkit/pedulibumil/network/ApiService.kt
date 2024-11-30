package com.bangkit.pedulibumil.network

import com.bangkit.pedulibumil.model.PredictionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RiskRequest(
    val name: String,
    val input: List<Int>
)

interface ApiService {
    @POST("/predict")
    fun predictRisk(@Body request: RiskRequest): Call<PredictionResponse>
}
