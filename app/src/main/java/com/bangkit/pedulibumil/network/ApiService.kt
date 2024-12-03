package com.bangkit.pedulibumil.network

import com.bangkit.pedulibumil.model.PredictionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class RiskRequest(
    val name: String,
    val input: List<Double>  // Mengubah tipe data menjadi List<Double> untuk mendukung nilai desimal
)

interface ApiService {
    @POST("/predict")
    fun predictRisk(@Body request: RiskRequest): Call<PredictionResponse>

    @GET("/get")
    fun getLatestPrediction(@Query("name") name: String): Call<Map<String, Any>>
}
