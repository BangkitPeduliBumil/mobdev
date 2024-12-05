package com.bangkit.pedulibumil.risk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.pedulibumil.MainActivity
import com.bangkit.pedulibumil.R
import com.bangkit.pedulibumil.history.HistoryEntity
import com.bangkit.pedulibumil.ui.history.HistoryViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val riskCategory = intent.getStringExtra("risk_category") ?: "Unknown"
        val tvRiskCategory = findViewById<TextView>(R.id.tvRiskCategory)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnClose = findViewById<Button>(R.id.btnClose)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        tvRiskCategory.text = "Risk Category: $riskCategory"

        btnSave.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis()
            historyViewModel.insertHistory(
                HistoryEntity(
                    timestamp = currentTimestamp,
                    riskCategory = riskCategory
                )
            )
            // Memberi notifikasi atau feedback bahwa data berhasil disimpan
            Toast.makeText(this, "History saved successfully", Toast.LENGTH_SHORT).show()
        }

        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
