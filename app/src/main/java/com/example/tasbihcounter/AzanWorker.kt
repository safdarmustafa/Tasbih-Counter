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
import kotlinx.coroutines.flow.first

class AzanWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val prayerName = inputData.getString("prayer") ?: "Prayer Time"
        val channelId = "azan_channel"

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        // 🔔 Get user selected mode from DataStore
        val azanMode = DataStoreManager
            .getAzanMode(applicationContext)
            .first()

        // 🚫 SILENT MODE → Do nothing
        if (azanMode == AzanMode.SILENT) {
            return Result.success()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val soundUri = Uri.parse(
                "android.resource://${applicationContext.packageName}/${R.raw.azan}"
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
                enableVibration(true)

                when (azanMode) {

                    AzanMode.FULL_SOUND -> {
                        setSound(soundUri, audioAttributes)
                    }

                    AzanMode.NOTIFICATION_ONLY -> {
                        setSound(null, null)
                    }

                    else -> {
                        setSound(null, null)
                    }
                }
            }

            notificationManager.createNotificationChannel(channel)
        }

        // 🔔 Build Notification
        val notification = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🕌 $prayerName")
            .setContentText("It's time for $prayerName prayer")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            prayerName.hashCode(),
            notification
        )

        return Result.success()
    }
}