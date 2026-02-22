package com.example.tasbihcounter

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object AzanScheduler {

    fun schedulePrayer(
        context: Context,
        prayerName: String,
        prayerTime: LocalDateTime
    ) {

        val delay = Duration.between(
            LocalDateTime.now(),
            prayerTime
        ).toMillis()

        if (delay <= 0) return

        val data = workDataOf("prayer" to prayerName)

        val request = OneTimeWorkRequestBuilder<AzanWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}