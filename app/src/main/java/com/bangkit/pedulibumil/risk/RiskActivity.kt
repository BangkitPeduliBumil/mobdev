package com.bangkit.pedulibumil.risk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.R
import com.bangkit.pedulibumil.model.PredictionResponse
import com.bangkit.pedulibumil.network.ApiClient
import com.bangkit.pedulibumil.network.RiskRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiskActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val inputTB = findViewById<TextInputEditText>(R.id.TB)
        val inputBB = findViewById<TextInputEditText>(R.id.BB)
        val inputSuhu = findViewById<TextInputEditText>(R.id.suhu)
        val inputSystolic = findViewById<TextInputEditText>(R.id.systolic)
        val inputDiastolic = findViewById<TextInputEditText>(R.id.diastolic)
        val inputGD = findViewById<TextInputEditText>(R.id.GD)
        val inputDJ = findViewById<TextInputEditText>(R.id.DJ)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil data nama dan usia dari Firestore
        db.collection("user").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("nama") ?: "Unknown"
                    val age = document.getString("umur")?.toIntOrNull() ?: 0

                    btnSubmit.setOnClickListener {
                        val tinggiBadan = inputTB.text.toString().toDoubleOrNull()
                        val beratBadan = inputBB.text.toString().toDoubleOrNull()
                        val suhu = inputSuhu.text.toString().toDoubleOrNull() ?: 0.0
                        val systolic = inputSystolic.text.toString().toIntOrNull() ?: 0
                        val diastolic = inputDiastolic.text.toString().toIntOrNull() ?: 0
                        val gulaDarah = inputGD.text.toString().toDoubleOrNull() ?: 0.0
                        val detakJantung = inputDJ.text.toString().toIntOrNull() ?: 0

                        // Validasi tinggi badan dan berat badan
                        if (tinggiBadan == null || beratBadan == null) {
                            Toast.makeText(
                                this,
                                "Tinggi badan dan berat badan harus diisi!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        // Hitung BMI
                        val tinggiMeter = tinggiBadan / 100
                        val bmi = beratBadan / (tinggiMeter * tinggiMeter)

                        // Kirim data ke API (urutan harus sesuai dengan yang diharapkan API)
                        val inputs = listOf(
                            age.toDouble(),    // Usia (diubah menjadi Double)
                            suhu,              // Suhu tubuh (dalam format desimal)
                            detakJantung.toDouble(), // Detak jantung (dalam format Double)
                            systolic.toDouble(), // Tekanan darah sistolik (dalam format Double)
                            diastolic.toDouble(), // Tekanan darah diastolik (dalam format Double)
                            bmi,               // BMI (dalam format desimal)
                            gulaDarah
                        )
                        Log.d("RiskActivity", "Inputs: $inputs")
                        sendRiskDataToApi(name, age, inputs)
                    }
                } else {
                    Toast.makeText(this, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("RiskActivity", "Error getting user data", e)
                Toast.makeText(this, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendRiskDataToApi(name: String, age: Int, inputs: List<Double>) {
        // Mengubah RiskRequest untuk menerima input berupa List<Double>
        ApiClient.instance.predictRisk(RiskRequest(name, inputs))
            .enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        Log.d("RiskActivity", "Response: $result")
                        val riskCategory = result?.risk_category ?: "Unknown"
                        val predictions = result?.prediction ?: emptyList()

                        // Simpan data dan navigasikan
                        savePredictionToFirestore(name, riskCategory, predictions) {
                            val intent = Intent(this@RiskActivity, ResultActivity::class.java)
                            intent.putExtra("risk_category", riskCategory)
                            intent.putExtra("predictions", predictions.toFloatArray())
                            startActivity(intent)
                        }
                    } else {
                        Log.e("RiskActivity", "API response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@RiskActivity, "Prediksi gagal!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    Log.e("RiskActivity", "API call failed: ${t.message}", t)
                    Toast.makeText(this@RiskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun savePredictionToFirestore(
        name: String,
        riskCategory: String,
        predictions: List<Float>,
        onSuccess: () -> Unit
    ) {
        val predictionData = hashMapOf(
            "nama" to name,
            "risk_category" to riskCategory,
            "predictions" to predictions
        )

        // Menggunakan koleksi "predictions" yang sudah ada
        val predictionRef = db.collection("predictions")

        // Menambahkan data prediksi baru ke dalam koleksi "predictions"
        predictionRef.add(predictionData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("RiskActivity", "Error saving prediction", e)
                Toast.makeText(this, "Gagal menyimpan prediksi", Toast.LENGTH_SHORT).show()
            }
    }

}
