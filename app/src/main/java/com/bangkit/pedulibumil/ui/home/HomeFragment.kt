package com.bangkit.pedulibumil.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.pedulibumil.BuildConfig
import com.bangkit.pedulibumil.chatbot.ChatbotActivity
import com.bangkit.pedulibumil.databinding.FragmentHomeBinding
import com.bangkit.pedulibumil.network.ApiClient
import com.bangkit.pedulibumil.risk.RiskActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private lateinit var viewModel: HomeViewModel

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
        setupViewModel()
        setupRecyclerView()
        observeViewModel()
        viewModel.fetchArticles("hamil, ibu, bayi")
    }

    private fun setupViewModel() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.ARTICLE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ArticleService::class.java)
        val factory = HomeViewModelFactory(service)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticles.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                binding.rvArticles.adapter = ArticleAdapter(articles) { articleLink ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(articleLink)
                    }
                    startActivity(intent)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = db.collection("user").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nama = document.data?.get("nama")?.toString()
                        val umur = document.data?.get("umur")?.toString()
                        val usiakandungan = document.data?.get("usiakandungan")?.toString()?.toIntOrNull()
                        val lastUpdated = document.data?.get("lastUpdated")?.toString()
                        val tanggalLahir = document.data?.get("tanggal_lahir")?.toString()

                        if (!tanggalLahir.isNullOrEmpty()) {
                            val newAge = checkAndUpdateAge(tanggalLahir, userRef)
                            binding.tvUmur.text = "$newAge Tahun"
                        }

                        if (usiakandungan != null) {
                            val updatedKandungan = checkAndUpdateKandungan(usiakandungan, lastUpdated, userRef)
                            binding.tvName.text = "Hi $nama"
                            binding.tvKandungan.text = "$updatedKandungan minggu kehamilan"
                            nama?.let { fetchLatestPrediction(it) }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("HomeFragment", "Gagal mengambil data pengguna", it)
                }

            binding.fabChatbot.setOnClickListener {
                val intent = Intent(requireContext(), ChatbotActivity::class.java)
                startActivity(intent)
            }

            binding.btnRisk.setOnClickListener {
                val intent = Intent(requireContext(), RiskActivity::class.java)
                startActivity(intent)
            }
        } else {
            Log.e("HomeFragment", "Pengguna tidak masuk")
        }
    }

    private fun checkAndUpdateAge(tanggalLahir: String, ref: DocumentReference): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthDate = dateFormat.parse(tanggalLahir) ?: return 0
        val today = Calendar.getInstance()
        val birthCalendar = Calendar.getInstance().apply { time = birthDate }

        var currentAge = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            currentAge -= 1
        }

        if (today.get(Calendar.DAY_OF_YEAR) == birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            ref.update("umur", currentAge + 1)
                .addOnSuccessListener { Log.d("HomeFragment", "Umur diperbarui menjadi ${currentAge}") }
                .addOnFailureListener { Log.e("HomeFragment", "Gagal memperbarui umur", it) }
        }

        return currentAge
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
                    val predictions = data?.get("input") as? List<Float> ?: emptyList()

                    binding.tvResiko.text = riskCategory
                    if (predictions.size >= 6) {
                        binding.tvHasilSuhu.text = "${formatToTwoDecimal(predictions[1])} °C"
                        binding.tvHasilHeartRate.text = "${formatToTwoDecimal(predictions[2])} bpm"
                        binding.tvHasilSystolic.text = "${formatToTwoDecimal(predictions[3])} mmHg"
                        binding.tvHasilDiastolic.text = "${formatToTwoDecimal(predictions[4])} mmHg"
                        binding.tvHasilBmi.text = "${formatToTwoDecimal(predictions[5])} kg/m²"
                        binding.tvHasilGulaDarah.text = "${formatToTwoDecimal(predictions[6])} mg/dL"
                    }
                } else {
                    Log.e("HomeFragment", "Failed to fetch prediction")
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching prediction", t)
            }
        })
    }

    private fun formatToTwoDecimal(value: Float): String {
        return String.format(Locale.getDefault(), "%.2f", value)
    }

    private fun checkAndUpdateKandungan(currentKandungan: Int, lastUpdated: String?, ref: DocumentReference): Int {
        val calendar = Calendar.getInstance()
        val today = calendar.time
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (dayOfWeek != Calendar.MONDAY) return currentKandungan
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val lastUpdatedDate = lastUpdated?.let { dateFormat.parse(it) }

        if (lastUpdatedDate == null || isNewWeek(lastUpdatedDate, today)) {
            val newKandungan = currentKandungan + 1
            ref.update(mapOf("usiakandungan" to newKandungan))
            return newKandungan
        }

        return currentKandungan
    }

    // Memeriksa apakah minggu baru telah dimulai
    private fun isNewWeek(lastUpdated: Date, today: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = lastUpdated
        val lastUpdatedWeek = calendar.get(Calendar.WEEK_OF_YEAR)

        calendar.time = today
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)

        return currentWeek != lastUpdatedWeek
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
