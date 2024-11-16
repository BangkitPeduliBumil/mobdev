package com.bangkit.test.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Link views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSave = findViewById(R.id.btnRegister)

        // Set onClickListener for the register button
        btnSave.setOnClickListener {
            // Get email and password from EditText fields
            val sEmail = etEmail.text.toString().trim()
            val sPassword = etPassword.text.toString().trim()

            // Check if email and password are empty
            if (sEmail.isEmpty() || sPassword.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register the user with email and password using Firebase Auth
            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If registration is successful, get the user and update the UI
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If registration fails, show a toast and update the UI
                        Toast.makeText(baseContext, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }

    // Function to update the UI after registration (navigate to MainActivity)
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // If user is not null, go to MainActivity
            val intent = Intent(this, IdentityActivity::class.java)
            startActivity(intent)
            finish() // Optional: finish SignupActivity so the user can't go back to it
        }
    }
}
