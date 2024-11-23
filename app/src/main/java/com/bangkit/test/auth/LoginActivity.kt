package com.bangkit.test.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.test.MainActivity
import com.bangkit.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//testing

class LoginActivity : AppCompatActivity() {
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi FirebaseAuth
        auth = Firebase.auth

        // Inisialisasi UI
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.tvRegister)

        // Tombol Login
        btnLogin.setOnClickListener {
            val sEmail = etEmail.text.toString().trim()
            val sPassword = etPassword.text.toString().trim()

            if (sEmail.isNotEmpty() && sPassword.isNotEmpty()) {
                if (isValidEmail(sEmail)) {
                    loginUser(sEmail, sPassword)
                } else {
                    Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Daftar
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi login dengan Firebase
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login berhasil
                    val user = auth.currentUser
                    Log.d("LoginActivity", "Login berhasil untuk user: ${user?.email}")
                    updateUI(user)
                } else {
                    // Login gagal
                    Log.w("LoginActivity", "Login gagal: ${task.exception?.message}")
                    Toast.makeText(
                        baseContext, "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    // Fungsi untuk update UI
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Jika user login, navigasi ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup LoginActivity agar tidak bisa kembali
        }
        // Jika user == null, tidak ada tindakan khusus
    }

    // Validasi email
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Cek status login saat aplikasi dimulai
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("LoginActivity", "User sudah login: ${currentUser.email}")
            updateUI(currentUser)
        }
    }
}
