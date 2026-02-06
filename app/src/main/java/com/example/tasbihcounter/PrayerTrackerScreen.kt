package com.example.tasbihcounter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PrayerTrackerScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    val prayerTimes = mapOf(
        "Fajr" to LocalTime.of(5, 10),
        "Dhuhr" to LocalTime.of(13, 15),
        "Asr" to LocalTime.of(16, 45),
        "Maghrib" to LocalTime.of(18, 30),
        "Isha" to LocalTime.of(19, 50)
    )

    // ✅ Reset if new day
    LaunchedEffect(Unit) {
        DataStoreManager.checkAndResetIfNewDay(context)
    }

    // ✅ Load saved states (Improved structure)
    var prayerStates by remember { mutableStateOf(prayers.associateWith { false }) }

    LaunchedEffect(Unit) {
        prayers.forEach { prayer ->
            launch {
                DataStoreManager.getPrayerState(context, prayer)
                    .collect { saved ->
                        prayerStates = prayerStates.toMutableMap().apply {
                            this[prayer] = saved
                        }
                    }
            }
        }
    }

    // ✅ Real-time clock
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000)
        }
    }

    val hourFormatter = DateTimeFormatter.ofPattern("hh")
    val minuteFormatter = DateTimeFormatter.ofPattern("mm")
    val secondFormatter = DateTimeFormatter.ofPattern("ss")
    val ampmFormatter = DateTimeFormatter.ofPattern("a")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val currentPrayer = when {
        currentTime.isBefore(prayerTimes["Fajr"]) -> "Isha"
        currentTime.isBefore(prayerTimes["Dhuhr"]) -> "Fajr"
        currentTime.isBefore(prayerTimes["Asr"]) -> "Dhuhr"
        currentTime.isBefore(prayerTimes["Maghrib"]) -> "Asr"
        currentTime.isBefore(prayerTimes["Isha"]) -> "Maghrib"
        else -> "Isha"
    }

    val nextPrayer = when (currentPrayer) {
        "Fajr" -> "Dhuhr"
        "Dhuhr" -> "Asr"
        "Asr" -> "Maghrib"
        "Maghrib" -> "Isha"
        else -> "Fajr"
    }

    val nextTime = prayerTimes[nextPrayer] ?: LocalTime.of(0, 0)

    val currentMinutes = currentTime.hour * 60 + currentTime.minute
    val nextMinutes = nextTime.hour * 60 + nextTime.minute

    val totalMinutes =
        if (nextMinutes > currentMinutes)
            nextMinutes - currentMinutes
        else
            (24 * 60 - currentMinutes) + nextMinutes

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    val countdownText =
        if (hours > 0) "${hours}h ${minutes}m"
        else "${minutes}m"

    val completedCount = prayerStates.values.count { it }
    val progress = completedCount / 5f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF241612), Color(0xFF140C09))
                )
            )
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ===== TOP SECTION =====
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "←",
                    fontSize = 22.sp,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable { onBack() }
                )

                Spacer(Modifier.height(20.dp))

                // Premium Clock
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFE2C07A).copy(alpha = 0.08f),
                            RoundedCornerShape(28.dp)
                        )
                        .padding(horizontal = 28.dp, vertical = 22.dp)
                ) {

                    Row(verticalAlignment = Alignment.Bottom) {

                        Text(
                            text = currentTime.format(hourFormatter),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xFFE2C07A)
                        )

                        Text(":", fontSize = 48.sp, color = Color(0xFFE2C07A))

                        Text(
                            text = currentTime.format(minuteFormatter),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xFFE2C07A)
                        )

                        Spacer(Modifier.width(8.dp))

                        Column {
                            Text(
                                text = currentTime.format(secondFormatter),
                                fontSize = 16.sp,
                                color = Color.White.copy(0.6f)
                            )
                            Text(
                                text = currentTime.format(ampmFormatter),
                                fontSize = 14.sp,
                                color = Color.White.copy(0.5f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = currentPrayer.uppercase(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2C07A)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Next: $nextPrayer in $countdownText",
                    fontSize = 13.sp,
                    color = Color.White.copy(0.7f)
                )

                Spacer(Modifier.height(24.dp))

                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = animatedProgress,
                        strokeWidth = 6.dp,
                        color = Color(0xFFE2C07A),
                        trackColor = Color.White.copy(0.08f),
                        modifier = Modifier.size(110.dp)
                    )

                    Text(
                        "$completedCount / 5",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ===== BOTTOM PRAYER GRID =====
            Column {

                prayers.chunked(2).forEach { rowPrayers ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        rowPrayers.forEach { prayer ->

                            val isCompleted = prayerStates[prayer] == true
                            val isCurrent = prayer == currentPrayer
                            val time = prayerTimes[prayer]

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isCurrent)
                                            Color(0xFFE2C07A).copy(alpha = 0.15f)
                                        else
                                            Color.White.copy(alpha = 0.05f),
                                        RoundedCornerShape(22.dp)
                                    )
                                    .clickable {
                                        val newValue = !isCompleted

                                        prayerStates =
                                            prayerStates.toMutableMap().apply {
                                                this[prayer] = newValue
                                            }

                                        scope.launch {
                                            DataStoreManager.savePrayerState(
                                                context,
                                                prayer,
                                                newValue
                                            )
                                        }
                                    }
                                    .padding(18.dp)
                            ) {

                                Column {

                                    Text(
                                        prayer,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isCurrent)
                                            Color(0xFFE2C07A)
                                        else
                                            Color.White
                                    )

                                    Text(
                                        time?.format(timeFormatter) ?: "",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(0.6f)
                                    )

                                    Spacer(Modifier.height(10.dp))

                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .background(
                                                if (isCompleted)
                                                    Color(0xFFE2C07A)
                                                else
                                                    Color.White.copy(0.25f),
                                                CircleShape
                                            )
                                    )
                                }
                            }
                        }

                        if (rowPrayers.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}
