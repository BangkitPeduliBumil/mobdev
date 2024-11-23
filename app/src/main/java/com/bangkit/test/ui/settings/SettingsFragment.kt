package com.bangkit.test.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.test.auth.LoginActivity
import com.bangkit.test.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // Properti binding untuk mengakses UI
    private val binding get() = _binding!!

    // Properti untuk GoogleSignInClient
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        // Menggunakan View Binding untuk inflasi layout
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Konfigurasi Google Sign-In Client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.bangkit.test.R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Mengatur tombol logout
        binding.btnLogout.setOnClickListener {
            // Proses logout dari Firebase
            Firebase.auth.signOut()

            // Proses logout dari Google Sign-In
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Navigasi ke LoginActivity setelah logout berhasil
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)

                    // Tutup fragment/activity saat ini
                    requireActivity().finish()
                } else {
                    // Menampilkan pesan jika gagal logout dari Google
                    binding.root.context.apply {
                        Toast.makeText(this, "Failed to sign out from Google", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
