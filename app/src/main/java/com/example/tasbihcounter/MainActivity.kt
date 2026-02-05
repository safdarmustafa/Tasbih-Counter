package com.example.tasbihcounter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape


import java.time.format.DateTimeFormatter

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.*
import com.example.tasbihcounter.ui.theme.SplashScreenJcTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            SplashScreenJcTheme {
                Scaffold { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tasbih",
        modifier = modifier
    ) {
        composable("tasbih") {
            IslamicSplash(
                onNavigateToPrayer = {
                    navController.navigate("prayer")
                }
            )
        }

        composable("prayer") {
            PrayerTrackerScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun IslamicSplash(
    modifier: Modifier = Modifier,
    onNavigateToPrayer: () -> Unit = {}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isMuted by remember { mutableStateOf(false) }

    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    val clickSoundId = remember {
        soundPool.load(context, R.raw.tasbihclick, 1)
    }

    fun playClick() {
        if (!isMuted) {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    val transition = rememberInfiniteTransition(label = "")
    val offset by transition.animateFloat(
        0f, 900f,
        infiniteRepeatable(tween(18000), RepeatMode.Reverse),
        label = ""
    )

    val gradient = Brush.linearGradient(
        listOf(Color(0xFF2A1C18), Color(0xFF3E2A24), Color(0xFF1A120F)),
        start = Offset.Zero,
        end = Offset(offset, offset)
    )

    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Assalamu Alaikum, Good Morning"
        in 12..16 -> "Assalamu Alaikum, Good Afternoon"
        in 17..20 -> "Assalamu Alaikum, Good Evening"
        else -> "Peaceful Night"
    }

    val quotes = listOf(
        "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ" to
                "Surely in the remembrance of Allah do hearts find peace",
        "إِنَّ مَعَ الْعُسْرِ يُسْرًا" to
                "Indeed, with hardship comes ease"
    )

    var quoteIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(6000)
            quoteIndex = (quoteIndex + 1) % quotes.size
        }
    }

    val dhikrList = listOf(
        "سُبْحَانَ اللَّهِ",
        "الْحَمْدُ لِلَّهِ",
        "اللَّهُ أَكْبَرُ"
    )

    var selectedDhikr by remember { mutableStateOf(dhikrList[0]) }
    var count by remember { mutableStateOf(0) }

    LaunchedEffect(selectedDhikr) {
        DataStoreManager.getCount(context, selectedDhikr)
            .collect { count = it }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(28.dp)
        ) {

            Text(
                "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE2C07A),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))
            Text(greeting, fontSize = 14.sp, color = Color(0xFFD0C4BC))

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.White.copy(0.05f), RoundedCornerShape(22.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = quoteIndex, label = "") { index ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            quotes[index].first,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2C07A),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            quotes[index].second,
                            fontSize = 15.sp,
                            color = Color.White.copy(0.9f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                dhikrList.forEach { dhikr ->
                    val selected = dhikr == selectedDhikr
                    Box(
                        modifier = Modifier
                            .background(
                                if (selected)
                                    Brush.linearGradient(
                                        listOf(Color(0xFFE2C07A), Color(0xFFB89B5E))
                                    )
                                else
                                    Brush.linearGradient(
                                        listOf(Color(0xFF3E2A24), Color(0xFF2A1C18))
                                    ),
                                RoundedCornerShape(30.dp)
                            )
                            .clickable {
                                selectedDhikr = dhikr
                                playClick()
                            }
                            .padding(horizontal = 22.dp, vertical = 12.dp)
                    ) {
                        Text(
                            dhikr,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) Color(0xFF2A1C18) else Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(34.dp))

            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(Color.White.copy(0.12f), CircleShape)
                    .clickable {
                        playClick()
                        count++
                        scope.launch {
                            DataStoreManager.saveCount(context, selectedDhikr, count)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("$count", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF3E2A24), RoundedCornerShape(22.dp))
                    .clickable {
                        playClick()
                        count = 0
                        scope.launch {
                            DataStoreManager.saveCount(context, selectedDhikr, 0)
                        }
                    }
                    .padding(horizontal = 26.dp, vertical = 10.dp)
            ) {
                Text("Reset", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFE2C07A), RoundedCornerShape(22.dp))
                    .clickable { onNavigateToPrayer() }
                    .padding(horizontal = 26.dp, vertical = 10.dp)
            ) {
                Text(
                    "Prayer Tracker",
                    color = Color(0xFF2A1C18),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PrayerTrackerScreen(onBack: () -> Unit) {

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    val prayerTimes = mapOf(
        "Fajr" to LocalTime.of(5, 10),
        "Dhuhr" to LocalTime.of(13, 15),
        "Asr" to LocalTime.of(16, 45),
        "Maghrib" to LocalTime.of(18, 30),
        "Isha" to LocalTime.of(19, 50)
    )

    var prayerStates by remember { mutableStateOf(prayers.associateWith { false }) }
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

                // Glass Clock Card
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

                        Text(
                            text = ":",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xFFE2C07A)
                        )

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

                Box(
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.06f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Next: $nextPrayer in $countdownText",
                        fontSize = 13.sp,
                        color = Color.White.copy(0.7f)
                    )
                }

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

            // ===== BOTTOM SECTION =====
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
                                        prayerStates =
                                            prayerStates.toMutableMap().apply {
                                                this[prayer] = !isCompleted
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
