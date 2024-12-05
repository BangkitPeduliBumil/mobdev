package com.bangkit.pedulibumil.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.pedulibumil.auth.LoginActivity
import com.bangkit.pedulibumil.databinding.FragmentSettingsBinding
import com.bangkit.pedulibumil.notifications.ReminderBroadcastReceiver
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.bangkit.pedulibumil.R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()

            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    binding.root.context.apply {
                        Toast.makeText(this, "Failed to sign out from Google", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Tambahkan pengaturan reminder
        binding.switchReminder.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                setMonthlyReminder()
                Toast.makeText(requireContext(), "Pengingat telah diaktifkan", Toast.LENGTH_SHORT).show()
            } else {
                cancelReminder()
            }
        }

        return root
    }

    private fun setMonthlyReminder() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Atur pengingat untuk waktu sebulan sekali
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 9) // Pukul 9 pagi
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 30,
            pendingIntent
        )
    }

    private fun cancelReminder() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Pengingat telah dibatalkan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
