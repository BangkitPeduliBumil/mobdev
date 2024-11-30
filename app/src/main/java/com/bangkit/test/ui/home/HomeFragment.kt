package com.bangkit.test.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.test.databinding.FragmentHomeBinding
import com.bangkit.test.chatbot.ChatbotActivity
import com.bangkit.test.risk.RiskActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Setup FAB for Chatbot
        safeBinding.fabChatbot.setOnClickListener {
            val intent = Intent(requireContext(), ChatbotActivity::class.java)
            startActivity(intent)
        }

        safeBinding.btnRisk.setOnClickListener {
            val intent = Intent(requireContext(), RiskActivity::class.java).apply {
                putExtra(RiskActivity.ARG_PARAM1, "value1")
                putExtra(RiskActivity.ARG_PARAM2, "value2")
            }
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
