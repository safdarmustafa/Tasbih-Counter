package com.example.splashscreenjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.splashscreenjc.ui.theme.SplashScreenJcTheme
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

    // 🌈 Animated Islamic gradient
    val transition = rememberInfiniteTransition(label = "")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F3D2E),
            Color(0xFF1B5E50),
            Color(0xFF0A1F44)
        ),
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset(offset, offset)
    )

    // 🕰 Greeting
    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Assalamu Alaikum, Good Morning"
        in 12..16 -> "Assalamu Alaikum, Good Afternoon"
        in 17..20 -> "Assalamu Alaikum, Good Evening"
        else -> "Peaceful Night"
    }

    // 📖 Ayah list
    val quotes = listOf(
        Triple(
            "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            "Surely in the remembrance of Allah do hearts find peace",
            "Qur'an 13:28"
        ),
        Triple(
            "إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            "Indeed, with hardship comes ease",
            "Qur'an 94:6"
        )
    )

    var quoteIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            quoteIndex = (quoteIndex + 1) % quotes.size
        }
    }

    // 📿 Tasbih state
    val dhikrList = listOf(
        "سُبْحَانَ اللَّهِ",
        "الْحَمْدُ لِلَّهِ",
        "اللَّهُ أَكْبَرُ"
    )
    var selectedDhikr by remember { mutableStateOf(dhikrList[0]) }
    var tasbihCount by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {

        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(1200)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(1200)
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(28.dp)
            ) {

                // 🕌 Bismillah
                Text(
                    text = "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD369),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = greeting,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 📖 Ayah
                Text(
                    text = quotes[quoteIndex].first,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD369),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = quotes[quoteIndex].second,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "— ${quotes[quoteIndex].third}",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.65f)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // 📿 Dhikr selector
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    dhikrList.forEach { dhikr ->
                        Box(
                            modifier = Modifier
                                .background(
                                    if (dhikr == selectedDhikr)
                                        Color(0xFF1F6F5B)
                                    else
                                        Color.White.copy(alpha = 0.2f),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    selectedDhikr = dhikr
                                    tasbihCount = 0
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = dhikr,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 📿 Counter + Reset
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(48.dp)
                            .background(
                                Color(0xFF1F6F5B),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                            )
                            .clickable { tasbihCount++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Count: $tasbihCount",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(48.dp)
                            .background(
                                Color(0xFF8B0000),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                            )
                            .clickable { tasbihCount = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Reset",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 🌟 Daily Niyyah
                Text(
                    text = "Make your niyyah:\nI do this dhikr only for Allah 🤍",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IslamicSplashPreview() {
    SplashScreenJcTheme {
        IslamicSplash()
    }
}
