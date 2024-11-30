package com.bangkit.pedulibumil.risk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val riskCategory = intent.getStringExtra("RISK_CATEGORY")
        val predictions = intent.getFloatArrayExtra("PREDICTIONS")

        val riskTextView = findViewById<TextView>(R.id.tvRiskCategory)
        val predictionTextView = findViewById<TextView>(R.id.tvPredictions)

        riskTextView.text = "Risk Category: $riskCategory"
        predictionTextView.text = "Predictions: ${predictions?.joinToString(", ")}"
    }
}
