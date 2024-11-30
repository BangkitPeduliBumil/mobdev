package com.bangkit.test.risk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.test.R

class RiskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        // Ambil data dari intent jika diperlukan
        val param1 = intent.getStringExtra(ARG_PARAM1)
        val param2 = intent.getStringExtra(ARG_PARAM2)

        // Gunakan param1 dan param2 sesuai kebutuhan
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
    }
}
