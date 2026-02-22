package com.example.tasbihcounter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AzanWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val prayerName = inputData.getString("prayer") ?: "Prayer Time"
        val channelId = "azan_channel"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // ✅ Create Notification Channel WITH sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val soundUri = Uri.parse(
                "android.resource://${context.packageName}/${R.raw.azan}"
            )

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val channel = NotificationChannel(
                channelId,
                "Prayer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Azan Notifications"
                setSound(soundUri, audioAttributes)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }

        // ✅ Build Notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🕌 $prayerName")
            .setContentText("It's time for $prayerName prayer")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(prayerName.hashCode(), notification)

        return Result.success()
    }
}