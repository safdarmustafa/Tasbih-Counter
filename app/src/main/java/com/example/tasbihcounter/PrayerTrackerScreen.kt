package com.example.tasbihcounter

import androidx.compose.animation.core.*
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.work.*
import java.time.LocalDateTime
import java.time.Duration
import java.util.concurrent.TimeUnit

@Composable
fun PremiumBackground(content: @Composable () -> Unit) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 700f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2B1C17), Color(0xFF140C09)),
                    center = Offset(offset, offset),
                    radius = 1200f
                )
            )
    ) {
        content()
    }
}

@Composable
fun PrayerTrackerScreen(
    viewModel: PrayerViewModel = viewModel(),
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val location = getUserLocation(context)
        location?.let {
            viewModel.updatePrayerTimes(it.first, it.second)
        }
    }

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    var prayerStates by remember {
        mutableStateOf(prayers.associateWith { false })
    }

    val times = viewModel.prayerTimes

    // 🔔 Schedule Real Azan Notifications
    LaunchedEffect(times) {
        if (times.isNotEmpty()) {
            times.forEach { (prayerName, prayerTime) ->
                val now = LocalDateTime.now()

                val prayerDateTime = now
                    .withHour(prayerTime.hour)
                    .withMinute(prayerTime.minute)
                    .withSecond(0)

                val delayMillis =
                    Duration.between(now, prayerDateTime).toMillis()

                if (delayMillis > 0) {

                    val request =
                        OneTimeWorkRequestBuilder<AzanWorker>()
                            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                            .setInputData(
                                workDataOf("prayer" to prayerName)
                            )
                            .build()

                    WorkManager
                        .getInstance(context)
                        .enqueueUniqueWork(
                            "azan_$prayerName",
                            ExistingWorkPolicy.REPLACE,
                            request
                        )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        prayers.forEach { prayer ->
            val saved = DataStoreManager
                .getPrayerState(context, prayer)
                .first()

            prayerStates = prayerStates.toMutableMap().apply {
                this[prayer] = saved
            }
        }
    }

    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = LocalTime.now()
        }
    }

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    val nextPrayer = if (times.isNotEmpty()) {
        when {
            currentTime.isBefore(times["Fajr"]) -> "Fajr"
            currentTime.isBefore(times["Dhuhr"]) -> "Dhuhr"
            currentTime.isBefore(times["Asr"]) -> "Asr"
            currentTime.isBefore(times["Maghrib"]) -> "Maghrib"
            currentTime.isBefore(times["Isha"]) -> "Isha"
            else -> "Fajr"
        }
    } else {
        "Fajr"
    }

    val nextTime = times[nextPrayer] ?: LocalTime.now()

    val nowInSeconds = currentTime.toSecondOfDay()
    val nextInSeconds = nextTime.toSecondOfDay()

    val remainingSeconds =
        if (nextInSeconds > nowInSeconds)
            nextInSeconds - nowInSeconds
        else
            (24 * 3600 - nowInSeconds) + nextInSeconds

    val animatedSeconds by animateIntAsState(
        targetValue = remainingSeconds,
        animationSpec = tween(500),
        label = ""
    )

    val displayHours = animatedSeconds / 3600
    val displayMinutes = (animatedSeconds % 3600) / 60
    val displaySecs = animatedSeconds % 60

    val completedCount = prayerStates.values.count { it }
    val progress = completedCount / 5f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = ""
    )

    PremiumBackground {

        Box(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val pulse by rememberInfiniteTransition(label = "")
                    .animateFloat(
                        initialValue = 0.7f,
                        targetValue = 1.2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = ""
                    )

                Box(contentAlignment = Alignment.Center) {

                    CircularProgressIndicator(
                        progress = animatedProgress,
                        strokeWidth = 6.dp,
                        color = Color(0xFFE2C07A),
                        trackColor = Color.White.copy(0.08f),
                        modifier = Modifier.size(190.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size((160 * pulse).dp)
                            .background(
                                Color(0xFFE2C07A).copy(alpha = 0.05f),
                                CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .shadow(12.dp, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFF3E2A24),
                                        Color(0xFF1A120F)
                                    )
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            currentTime.format(formatter),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = viewModel.cityName,
                        fontSize = 14.sp,
                        color = Color.White.copy(0.7f)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Next Prayer",
                        fontSize = 12.sp,
                        color = Color.White.copy(0.5f)
                    )

                    Spacer(Modifier.height(6.dp))

                    // 🔔 Azan Mode Toggle
                    val azanMode by DataStoreManager
                        .getAzanMode(context)
                        .collectAsState(initial = AzanMode.FULL_SOUND)

                    Text(
                        text = when (azanMode) {
                            AzanMode.FULL_SOUND -> "🔊 Full Azan"
                            AzanMode.NOTIFICATION_ONLY -> "🔔 Notification Only"
                            AzanMode.SILENT -> "🔕 Silent"
                        },
                        fontSize = 12.sp,
                        color = Color(0xFFE2C07A),
                        modifier = Modifier.clickable {
                            scope.launch {
                                val nextMode = when (azanMode) {
                                    AzanMode.SILENT -> AzanMode.FULL_SOUND
                                    AzanMode.FULL_SOUND -> AzanMode.NOTIFICATION_ONLY
                                    AzanMode.NOTIFICATION_ONLY -> AzanMode.SILENT
                                }
                                DataStoreManager.saveAzanMode(context, nextMode)
                            }
                        }
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        nextPrayer.uppercase(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE2C07A)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "in %02dh %02dm %02ds".format(
                            displayHours,
                            displayMinutes,
                            displaySecs
                        ),
                        fontSize = 14.sp,
                        color = Color.White.copy(0.7f),
                        modifier = Modifier.animateContentSize()
                    )
                }

                Column {
                    prayers.chunked(2).forEach { rowPrayers ->

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            rowPrayers.forEach { prayer ->

                                val isCompleted = prayerStates[prayer] == true
                                val time = times[prayer]

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .shadow(6.dp, RoundedCornerShape(20.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF2A1C17),
                                                    Color(0xFF2A1C17),
                                                    Color.White.copy(alpha = 0.05f)
                                                )
                                            ),
                                            RoundedCornerShape(20.dp)
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
                                        .padding(20.dp)
                                ) {

                                    Column {

                                        Text(
                                            prayer,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )

                                        Spacer(Modifier.height(4.dp))

                                        Text(
                                            time?.format(formatter) ?: "--:--",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(0.6f)
                                        )

                                        Spacer(Modifier.height(14.dp))

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

                        Spacer(Modifier.height(18.dp))
                    }
                }
            }
        }
    }
}