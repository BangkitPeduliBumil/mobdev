package com.bangkit.pedulibumil.risk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.R
import com.bangkit.pedulibumil.model.PredictionResponse
import com.bangkit.pedulibumil.network.ApiClient
import com.bangkit.pedulibumil.network.RiskRequest
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        val inputTB = findViewById<TextInputLayout>(R.id.inputTB)
        val inputBB = findViewById<TextInputLayout>(R.id.inputBB)
        val inputSuhu = findViewById<TextInputLayout>(R.id.inputSuhu)
        val inputTD = findViewById<TextInputLayout>(R.id.inputTD)
        val inputGD = findViewById<TextInputLayout>(R.id.inputGD)
        val inputUsia = findViewById<TextInputLayout>(R.id.inputUsia)
        val inputJantung = findViewById<TextInputLayout>(R.id.textInputLayout)
        val submitButton = findViewById<Button>(R.id.button3)

        submitButton.setOnClickListener {
            val name = "username_from_firestore"
            val inputs = listOf(
                inputTB.editText?.text.toString().toIntOrNull() ?: 0,
                inputBB.editText?.text.toString().toIntOrNull() ?: 0,
                inputSuhu.editText?.text.toString().toIntOrNull() ?: 0,
                inputTD.editText?.text.toString().toIntOrNull() ?: 0,
                inputGD.editText?.text.toString().toIntOrNull() ?: 0,
                inputUsia.editText?.text.toString().toIntOrNull() ?: 0,
                inputJantung.editText?.text.toString().toIntOrNull() ?: 0
            )

            ApiClient.instance.predictRisk(RiskRequest(name, inputs))
                .enqueue(object : Callback<PredictionResponse> {
                    override fun onResponse(
                        call: Call<PredictionResponse>,
                        response: Response<PredictionResponse>
                    ) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            val intent = Intent(this@RiskActivity, ResultActivity::class.java).apply {
                                putExtra("RISK_CATEGORY", result?.risk_category)
                                putExtra("PREDICTIONS", result?.prediction?.toFloatArray())
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RiskActivity, "Prediction failed!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                        Toast.makeText(this@RiskActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
