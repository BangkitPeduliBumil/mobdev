package com.bangkit.pedulibumil.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bangkit.pedulibumil.R

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val channelId = "monthly_reminder"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat Notification Channel (untuk Android O+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Monthly Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Buat dan tampilkan notifikasi
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_none_24) // Ganti dengan ikon notifikasi
            .setContentTitle("Cek Risiko Anda!")
            .setContentText("Saatnya untuk cek risiko kesehatan Anda bulan ini.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
