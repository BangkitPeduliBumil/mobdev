package com.bangkit.pedulibumil.risk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.MainActivity
import com.bangkit.pedulibumil.R

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val riskCategory = intent.getStringExtra("risk_category") ?: "Unknown"

        val tvRiskCategory = findViewById<TextView>(R.id.tvRiskCategory)
        val btnClose = findViewById<Button>(R.id.btnClose)

        tvRiskCategory.text = "Risk Category: $riskCategory"

        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
