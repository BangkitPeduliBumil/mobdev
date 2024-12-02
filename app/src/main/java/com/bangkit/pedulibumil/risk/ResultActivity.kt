package com.bangkit.pedulibumil.risk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val riskCategory = intent.getStringExtra("risk_category") ?: "Unknown"
        val predictions = intent.getFloatArrayExtra("predictions") ?: floatArrayOf()

        val tvRiskCategory = findViewById<TextView>(R.id.tvRiskCategory)
        val tvPredictions = findViewById<TextView>(R.id.tvPredictions)

        tvRiskCategory.text = "Risk Category: $riskCategory"
        tvPredictions.text = "Predictions: ${predictions.joinToString(", ")}"
    }
}
