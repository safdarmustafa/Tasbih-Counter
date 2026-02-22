package com.example.tasbihcounter

import kotlinx.datetime.Instant
import java.time.LocalTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Instant.toLocalTimeSafe(): LocalTime {
    val zoneId = java.time.ZoneId.systemDefault()
    val localDateTime =
        java.time.Instant.ofEpochMilli(this.toEpochMilliseconds())
            .atZone(zoneId)
            .toLocalDateTime()

    return LocalTime.of(localDateTime.hour, localDateTime.minute)
}