package com.bangkit.pedulibumil.model

data class PredictionResponse(
    val prediction: List<Float>,
    val risk_category: String
)
