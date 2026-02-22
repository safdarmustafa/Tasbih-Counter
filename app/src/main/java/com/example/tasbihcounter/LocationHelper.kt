package com.example.tasbihcounter

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getLastKnownLocation(context: Context): Pair<Double, Double>? {

    val fusedClient =
        LocationServices.getFusedLocationProviderClient(context)

    val task = fusedClient.lastLocation

    if (task.isComplete && task.result != null) {
        val location = task.result
        return Pair(location.latitude, location.longitude)
    }

    return null
}