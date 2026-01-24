package com.example.tasbihcounter
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tasbihcounter.ui.theme.SplashScreenJcTheme
import kotlinx.coroutines.delay
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            SplashScreenJcTheme {
                Scaffold { innerPadding ->
                    IslamicSplash(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IslamicSplash(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var isMuted by remember { mutableStateOf(false) }

    /* 🔊 SoundPool (REAL DEVICE SAFE) */
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
        soundPool.load(context, com.example.tasbihcounter.R.raw.tasbihclick, 1)
    }

    fun playClick() {
        if (!isMuted) {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    /* 🌈 Brown gradient */
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

    /* 🕰 Greeting */
    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Assalamu Alaikum, Good Morning"
        in 12..16 -> "Assalamu Alaikum, Good Afternoon"
        in 17..20 -> "Assalamu Alaikum, Good Evening"
        else -> "Peaceful Night"
    }

    /* 📖 Quotes */
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

    /* 📿 Dhikr */
    val dhikrList = listOf(
        "سُبْحَانَ اللَّهِ",
        "الْحَمْدُ لِلَّهِ",
        "اللَّهُ أَكْبَرُ"
    )

    var selectedDhikr by remember { mutableStateOf(dhikrList[0]) }
    var count by remember { mutableStateOf(0) }

    Box(
        modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        /* 🔇 MUTE TOGGLE (BOTTOM LEFT) */
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.White.copy(0.12f), RoundedCornerShape(18.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isMuted = !isMuted
                    playClick()
                }
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (isMuted) "🔇 Muted" else "🔊 Sound",
                fontSize = 12.sp,
                color = Color.White
            )
        }

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

            /* 📖 Quote */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.White.copy(0.05f), RoundedCornerShape(22.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = quoteIndex,
                    animationSpec = tween(1200, easing = FastOutSlowInEasing),
                    label = ""
                ) { index ->
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

            /* 📿 Dhikr Selector */
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
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                selectedDhikr = dhikr
                                count = 0
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

            /* 🔵 Counter */
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(Color.White.copy(0.12f), CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        count++
                        playClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$count",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))

            /* 🔄 Reset */
            Box(
                modifier = Modifier
                    .background(Color(0xFF3E2A24), RoundedCornerShape(22.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        count = 0
                        playClick()
                    }
                    .padding(horizontal = 26.dp, vertical = 10.dp)
            ) {
                Text("Reset", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIslamicSplash() {
    SplashScreenJcTheme { IslamicSplash() }
}
