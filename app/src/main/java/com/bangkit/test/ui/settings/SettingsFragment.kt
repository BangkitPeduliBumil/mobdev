package com.bangkit.test.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.test.auth.LoginActivity
import com.bangkit.test.databinding.FragmentSettingsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // Properti binding untuk mengakses UI
    private val binding get() = _binding!!

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

        // Observasi teks dari ViewModel
        settingsViewModel.text.observe(viewLifecycleOwner) {
            binding.textNotifications.text = it
        }

        // Mengatur tombol logout
        binding.btnLogout.setOnClickListener {
            // Proses logout dengan Firebase
            Firebase.auth.signOut()

            // Navigasi ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            // Tutup fragment/activity saat ini
            requireActivity().finish()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
