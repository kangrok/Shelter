package com.example.shelter.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationHelper(private val mContext: Context) : LocationListener {

    companion object {
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        const val MIN_TIME_BW_UPDATES = (600000).toLong()
    }

    var isGPSEnabled = false
    var isNetworkEnabled = false

    var locationManager: LocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

    init {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getCurrenLocationUsingNetwork() : Location?{
        return if (!isNetworkEnabled) {
            Log.v("TAG ","Network not enabled!")
            null
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
            )
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationUsingGPS(): Location? {
        var location: Location? = null
        if (checkPermissions()) {
            Log.v("TAG ","No permission Granted")
        } else {
            if (!isGPSEnabled) {
                Log.v("TAG ","GPS not enabled!")
            } else {
                // If GPS enabled, get latitude/longitude using GPS Services
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                )
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            }
        }
        return location
    }

    fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
    }

    override fun onLocationChanged(location: Location) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

}