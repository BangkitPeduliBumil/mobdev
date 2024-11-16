package com.bangkit.test.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.test.MainActivity
import com.bangkit.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IdentityActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etKandungan: EditText
    private lateinit var btnSubmit: Button

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity)

        // Inisialisasi EditText dan Button
        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etKandungan = findViewById(R.id.etKandungan)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Set OnClickListener untuk tombol submit
        btnSubmit.setOnClickListener {
            val sName = etName.text.toString().trim()
            val sAge = etAge.text.toString().trim()
            val sKandungan = etKandungan.text.toString().trim()

            // Validasi input agar tidak kosong
            if (sName.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (sAge.isEmpty()) {
                Toast.makeText(this, "Umur tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (sKandungan.isEmpty()) {
                Toast.makeText(this, "Usia kandungan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Membuat HashMap untuk data user
            val userMap = hashMapOf(
                "nama" to sName,
                "umur" to sAge,
                "usiakandungan" to sKandungan
            )

            // Mendapatkan userId dari FirebaseAuth
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Menyimpan data ke Firestore
            db.collection("user").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: finish SignupActivity so the user can't go back to it
                }
                .addOnFailureListener { e ->
                    Log.e("Identity", "Error saving data", e)
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
