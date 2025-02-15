package com.findyou_professionalapp.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

object LocationHelper {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context, callback: (latitude: Double, longitude: Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_LONG).show()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    callback(it.latitude, it.longitude)

                } ?: run {
                    Log.e("LocationHelper", "Error: ${location.toString()}")
                }
            }
            .addOnFailureListener {
                Log.e("LocationHelper", "Error: ${it.message}")
            }
    }

    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
            } else {
                "Address not found"
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Error: ${e.message}")
            "Unable to fetch address"
        }
    }
}
