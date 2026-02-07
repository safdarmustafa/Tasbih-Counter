package com.example.tasbihcounter

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
suspend fun getUserLocation(context: Context): Pair<Double, Double>? {

    val fusedClient = LocationServices
        .getFusedLocationProviderClient(context)

    return suspendCancellableCoroutine { continuation ->

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        val callback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                val location: Location? = result.lastLocation

                if (location != null) {
                    continuation.resume(
                        Pair(location.latitude, location.longitude)
                    )
                    fusedClient.removeLocationUpdates(this)
                }
            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            callback,
            context.mainLooper
        )
    }
}
