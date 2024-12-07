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

        // Ambil data dari Intent
        val riskCategory = intent.getStringExtra("risk_category") ?: "Unknown"

        // Hubungkan dengan komponen UI
        val tvRiskCategory = findViewById<TextView>(R.id.tvRiskCategory)
        val tvAlasanContent = findViewById<TextView>(R.id.tvAlasanContent)
        val tvLangkahContent = findViewById<TextView>(R.id.tvLangkahContent)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnClose = findViewById<Button>(R.id.btnClose)

        // Inisialisasi ViewModel
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Tampilkan kategori risiko
        tvRiskCategory.text = riskCategory

        // Ambil alasan dan langkah sesuai kategori risiko
        val (alasan, langkah) = getRiskDetails(riskCategory)
        tvAlasanContent.text = alasan
        tvLangkahContent.text = langkah

        // Simpan ke riwayat ketika tombol "Simpan" ditekan
        btnSave.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis()
            historyViewModel.insertHistory(
                HistoryEntity(
                    timestamp = currentTimestamp,
                    riskCategory = riskCategory
                )
            )
            Toast.makeText(this, "Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Tutup aktivitas dan kembali ke MainActivity
        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    /**
     * Mengembalikan alasan dan langkah perawatan berdasarkan kategori risiko.
     */
    private fun getRiskDetails(riskCategory: String): Pair<String, String> {
        return when (riskCategory) {
            "Resiko Tinggi" -> {
                val alasan = """
                    • Parameter kesehatan menunjukkan komplikasi serius, seperti hipertensi (≥140/90), gula darah sangat tinggi, atau anemia berat.
                    • Faktor risiko tambahan: usia <18 atau >40 tahun, kehamilan ganda, atau riwayat komplikasi kehamilan sebelumnya.
                    • Adanya penyakit kronis seperti penyakit jantung, asma, atau ginjal.
                """.trimIndent()

                val langkah = """
                    1. Segera rujuk ke dokter spesialis kandungan untuk pemantauan lebih intensif.
                    2. Kelola penyakit penyerta seperti kontrol gula darah, hipertensi, atau pengobatan anemia.
                    3. Lakukan pemeriksaan tambahan, seperti USG berkala, detak jantung janin, dan tes laboratorium.
                    4. Kurangi aktivitas fisik berat, istirahat cukup, dan hindari stres.
                    5. Persiapkan rencana persalinan darurat, termasuk kemungkinan operasi caesar jika diperlukan.
                """.trimIndent()

                Pair(alasan, langkah)
            }

            "Resiko Sedang" -> {
                val alasan = """
                    • Beberapa parameter kesehatan menunjukkan potensi risiko, seperti tekanan darah sedikit tinggi atau gula darah mendekati batas atas.
                    • Usia Anda berada di bawah 20 atau di atas 35 tahun.
                    • Riwayat kehamilan sebelumnya dengan komplikasi ringan atau ada riwayat penyakit kronis ringan dalam keluarga (misal hipertensi, diabetes).
                """.trimIndent()

                val langkah = """
                    1. Tingkatkan frekuensi pemeriksaan antenatal untuk pemantauan lebih dekat.
                    2. Konsultasikan dengan dokter pola makan yang lebih ketat (misalnya diet rendah garam atau rendah gula).
                    3. Pertimbangkan suplemen tambahan sesuai anjuran dokter (misalnya kalsium atau DHA).
                    4. Hindari aktivitas berat yang dapat menyebabkan stres atau kelelahan.
                    5. Diskusikan potensi metode persalinan untuk memastikan kesiapan sejak dini.
                """.trimIndent()

                Pair(alasan, langkah)
            }

            "Resiko Rendah" -> {
                val alasan = """
                    • Parameter kesehatan Anda berada dalam batas normal (tekanan darah, gula darah, suhu tubuh, dll.).
                    • Tidak ada kondisi medis kronis atau faktor risiko lain yang terdeteksi.
                    • Anda berada dalam rentang usia reproduktif ideal (20-35 tahun) tanpa riwayat komplikasi kehamilan sebelumnya.
                """.trimIndent()

                val langkah = """
                    1. Lakukan pemeriksaan antenatal rutin (setiap 4 minggu di trimester pertama, lebih sering di trimester berikutnya).
                    2. Konsumsi makanan bergizi seimbang (protein, karbohidrat, lemak sehat, vitamin, dan mineral).
                    3. Jaga aktivitas fisik dengan olahraga ringan seperti jalan kaki atau yoga prenatal.
                    4. Hindari stres, rokok, alkohol, dan konsumsi kafein berlebih.
                    5. Pastikan kebutuhan tidur tercukupi, sekitar 7-9 jam per malam.
                """.trimIndent()

                Pair(alasan, langkah)
            }

            else -> {
                val alasan = """
                    Kategori risiko tidak dikenali.
                    Mohon periksa kembali data risiko yang dimasukkan.
                """.trimIndent()

                val langkah = """
                    1. Segera hubungi penyedia layanan kesehatan untuk informasi lebih lanjut.
                    2. Pastikan data kategori risiko telah diinput dengan benar.
                """.trimIndent()

                Pair(alasan, langkah)
            }
        }
    }
}
