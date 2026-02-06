package com.example.tasbihcounter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlin.math.*

@Composable
fun QiblaScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF241612), Color(0xFF140C09))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("Location permission required", color = Color.White)
        }
    } else {
        PremiumCompass()
    }
}

@SuppressLint("MissingPermission")
@Composable
fun PremiumCompass() {

    val context = LocalContext.current
    val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var azimuth by remember { mutableStateOf(0f) }
    var qiblaBearing by remember { mutableStateOf(0f) }

    val kaabaLat = 21.4225
    val kaabaLng = 39.8262

    // Get best last location
    LaunchedEffect(Unit) {
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null

        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }

        bestLocation?.let {
            qiblaBearing = calculateQiblaDirection(
                it.latitude,
                it.longitude,
                kaabaLat,
                kaabaLng
            )
        }
    }

    DisposableEffect(Unit) {

        val accelerometer =
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer =
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val listener = object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent) {

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER ->
                        System.arraycopy(event.values, 0, gravity, 0, 3)

                    Sensor.TYPE_MAGNETIC_FIELD ->
                        System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                }

                if (SensorManager.getRotationMatrix(
                        rotationMatrix,
                        null,
                        gravity,
                        geomagnetic
                    )
                ) {
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthRad = orientation[0]
                    val azimuthDeg =
                        Math.toDegrees(azimuthRad.toDouble()).toFloat()
                    azimuth = (azimuthDeg + 360) % 360
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        sensorManager.registerListener(
            listener,
            magnetometer,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue = -azimuth,
        animationSpec = tween(400),
        label = ""
    )

    val difference = ((qiblaBearing - azimuth + 360) % 360)
    val isAligned = difference < 5 || difference > 355

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF241612), Color(0xFF140C09))
                )
            )
    ) {

        Text(
            text = "Qibla ${qiblaBearing.toInt()}°",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE2C07A)
        )

        Box(contentAlignment = Alignment.Center) {

            // ROTATE WHOLE COMPASS (ticks + directions)
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .graphicsLayer {
                        rotationZ = animatedRotation
                    }
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {

                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0xFF3E2A24), Color(0xFF1A120F))
                        )
                    )

                    val radius = size.minDimension / 2

                    for (i in 0 until 360 step 10) {
                        val angle = Math.toRadians(i.toDouble())

                        val start = Offset(
                            center.x + (radius - 30) * cos(angle).toFloat(),
                            center.y + (radius - 30) * sin(angle).toFloat()
                        )

                        val end = Offset(
                            center.x + radius * cos(angle).toFloat(),
                            center.y + radius * sin(angle).toFloat()
                        )

                        drawLine(
                            color = Color(0xFFD0C4BC),
                            start = start,
                            end = end,
                            strokeWidth = if (i % 90 == 0) 6f else 3f
                        )
                    }
                }

                Text("N", Modifier.align(Alignment.TopCenter), color = Color(0xFFE2C07A))
                Text("S", Modifier.align(Alignment.BottomCenter), color = Color(0xFFD0C4BC))
                Text("E", Modifier.align(Alignment.CenterEnd), color = Color(0xFFD0C4BC))
                Text("W", Modifier.align(Alignment.CenterStart), color = Color(0xFFD0C4BC))
            }

            // KAABA FIXED (does NOT rotate)
            Image(
                painter = painterResource(id = R.drawable.kaaba),
                contentDescription = "Kaaba",
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.TopCenter)
            )
        }

        Text(
            text = if (isAligned) "Aligned ✓"
            else "Turn ${difference.toInt()}°",
            fontSize = 18.sp,
            color = if (isAligned)
                Color(0xFF4CAF50)
            else
                Color(0xFFD0C4BC)
        )
    }
}

fun calculateQiblaDirection(
    userLat: Double,
    userLng: Double,
    kaabaLat: Double,
    kaabaLng: Double
): Float {

    val lat1 = Math.toRadians(userLat)
    val lat2 = Math.toRadians(kaabaLat)
    val deltaLng = Math.toRadians(kaabaLng - userLng)

    val y = sin(deltaLng) * cos(lat2)
    val x =
        cos(lat1) * sin(lat2) -
                sin(lat1) * cos(lat2) *
                cos(deltaLng)

    val bearing = Math.toDegrees(atan2(y, x))

    return ((bearing + 360) % 360).toFloat()
}
