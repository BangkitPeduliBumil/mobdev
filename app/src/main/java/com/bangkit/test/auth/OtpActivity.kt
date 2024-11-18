package com.bangkit.test.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bangkit.test.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class OtpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var pass: String
    private var otpCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ambil elemen UI menggunakan findViewById
        val showEmail: TextView = findViewById(R.id.showEmail)
        val otp1: EditText = findViewById(R.id.otp1)
        val otp2: EditText = findViewById(R.id.otp2)
        val otp3: EditText = findViewById(R.id.otp3)
        val otp4: EditText = findViewById(R.id.otp4)
        val otp5: EditText = findViewById(R.id.otp5)
        val otp6: EditText = findViewById(R.id.otp6)
        val button: Button = findViewById(R.id.button)
        val tvResend: TextView = findViewById(R.id.tvResend)

        // Ambil data dari intent
        email = intent.getStringExtra("email").orEmpty()
        pass = intent.getStringExtra("pass").orEmpty()

        // Tampilkan email pengguna
        showEmail.text = email

        // Generate dan kirim OTP
        generateAndSendOtp()

        // Setup listener untuk OTP field
        setupOtpFields(otp1, otp2, otp3, otp4, otp5, otp6)

        button.setOnClickListener {
            val enteredOtp = collectOtpInput(otp1, otp2, otp3, otp4, otp5, otp6)
            if (enteredOtp == otpCode) {
                // OTP benar, buat akun
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, IdentityActivity::class.java)
                        startActivity(intent)
                        finish() // Tutup aktivitas ini
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kode OTP salah!", Toast.LENGTH_SHORT).show()
            }
        }

        tvResend.setOnClickListener {
            generateAndSendOtp()
            Toast.makeText(this, "Kode OTP dikirim ulang!", Toast.LENGTH_SHORT).show()
        }
    }

    // Generate dan kirim OTP
    private fun generateAndSendOtp() {
        otpCode = Random.nextInt(100000, 999999).toString() // Generate kode 6 digit
        val subject = "Kode OTP Anda"
        val message = "Kode OTP Anda adalah $otpCode. Jangan bagikan kode ini kepada siapapun!"

        // Gunakan utilitas pengiriman email
        SendMailTask("otpverif039@gmail.com", "ozpsyulerzjekfrz", email, subject, message).execute()
    }

    // Setup input OTP
    private fun setupOtpFields(
        otp1: EditText,
        otp2: EditText,
        otp3: EditText,
        otp4: EditText,
        otp5: EditText,
        otp6: EditText
    ) {
        otp1.doOnTextChanged { text, _, _, _ -> if (!text.isNullOrEmpty()) otp2.requestFocus() }
        otp2.doOnTextChanged { text, _, _, _ -> if (!text.isNullOrEmpty()) otp3.requestFocus() else otp1.requestFocus() }
        otp3.doOnTextChanged { text, _, _, _ -> if (!text.isNullOrEmpty()) otp4.requestFocus() else otp2.requestFocus() }
        otp4.doOnTextChanged { text, _, _, _ -> if (!text.isNullOrEmpty()) otp5.requestFocus() else otp3.requestFocus() }
        otp5.doOnTextChanged { text, _, _, _ -> if (!text.isNullOrEmpty()) otp6.requestFocus() else otp4.requestFocus() }
        otp6.doOnTextChanged { text, _, _, _ -> if (text.isNullOrEmpty()) otp5.requestFocus() }
    }

    // Kumpulkan input OTP
    private fun collectOtpInput(
        otp1: EditText,
        otp2: EditText,
        otp3: EditText,
        otp4: EditText,
        otp5: EditText,
        otp6: EditText
    ): String {
        return otp1.text.toString() +
                otp2.text.toString() +
                otp3.text.toString() +
                otp4.text.toString() +
                otp5.text.toString() +
                otp6.text.toString()
    }
}
