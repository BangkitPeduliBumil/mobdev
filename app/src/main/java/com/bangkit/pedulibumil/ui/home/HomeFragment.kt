package com.bangkit.pedulibumil.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.pedulibumil.chatbot.ChatbotActivity
import com.bangkit.pedulibumil.network.ApiClient
import com.bangkit.pedulibumil.risk.RiskActivity
import com.bangkit.pedulibumil.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        val safeBinding = _binding ?: return
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val ref = db.collection("user").document(userId)
            ref.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nama = document.data?.get("nama")?.toString()
                        val umur = document.data?.get("umur")?.toString()
                        val usiakandungan = document.data?.get("usiakandungan")?.toString()

                        safeBinding.tvName.text = "Hi $nama"
                        safeBinding.tvUmur.text = umur
                        safeBinding.tvKandungan.text = "$usiakandungan minggu kehamilan"

                        nama?.let { fetchLatestPrediction(it) }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }

        safeBinding.fabChatbot.setOnClickListener {
            val intent = Intent(requireContext(), ChatbotActivity::class.java)
            startActivity(intent)
        }

        safeBinding.btnRisk.setOnClickListener {
            val intent = Intent(requireContext(), RiskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchLatestPrediction(name: String) {
        ApiClient.instance.getLatestPrediction(name).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    val data = result["data"] as? Map<*, *>
                    val riskCategory = data?.get("risk_category")?.toString() ?: "Unknown"

                    binding.tvResiko.text = "$riskCategory"
                } else {
                    Log.e("HomeFragment", "Failed to fetch prediction: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch latest prediction!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching prediction", t)
                Toast.makeText(
                    requireContext(),
                    "Error fetching prediction: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //test
    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
