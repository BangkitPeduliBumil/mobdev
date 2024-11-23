package com.bangkit.test.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.test.MainActivity
import com.bangkit.test.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleLogin: LinearLayout
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi FirebaseAuth
        auth = Firebase.auth

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Client ID dari Firebase Console
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inisialisasi UI
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogleLogin = findViewById(R.id.llGoogleLogin)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.tvRegister)

        // Tombol Login dengan Email & Password
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

        // Tombol Login dengan Google
        btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }

        // Tombol Daftar
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("LoginActivity", "Login berhasil untuk user: ${user?.email}")
                    updateUI(user)
                } else {
                    Log.w("LoginActivity", "Login gagal: ${task.exception?.message}")
                    Toast.makeText(
                        this, "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("LoginActivity", "Google Sign-In berhasil, ID Token: ${account.idToken}")
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("LoginActivity", "Google Sign-In gagal: ${e.message}")
                Toast.makeText(this, "Google Sign-In gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false
                    Log.d("LoginActivity", "Firebase Authentication berhasil untuk user: ${user?.email}, isNewUser: $isNewUser")

                    if (isNewUser) {
                        val intent = Intent(this, IdentityActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        updateUI(user)
                    }
                } else {
                    Log.w("LoginActivity", "Firebase Authentication gagal: ${task.exception?.message}")
                    Toast.makeText(
                        this, "Authentication gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("LoginActivity", "User sudah login: ${currentUser.email}")
            updateUI(currentUser)
        }
    }
}
