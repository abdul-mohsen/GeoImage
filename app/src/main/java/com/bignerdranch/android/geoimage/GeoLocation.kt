package com.bignerdranch.android.geoimage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.bignerdranch.android.geoimage.model.DeviceState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber


class GeoLocation(
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1,
    private val updateLocation: (Location) -> Unit,
) : LocationListener {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var GPSConnnected = false

    fun initFuesdLocationProviderClient(context: Context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }
    override fun onLocationChanged(location: Location) {
        Timber.d("Location changed")
        updateLocation(location)
    }

    private fun requestUpdateLocation(context: Context){
        val locationManager =  context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if(locationManager != null){
            val isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGPS){
                val gpsOptionsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(context, gpsOptionsIntent, null)
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    1.0f,
                    this
                )
            }
        }
    }

    fun enableMyLocation(activity: Activity, updateDeviceState: (DeviceState) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener{ location ->
                Timber.d( "Location got updated")
                if (location != null) {
                    updateLocation(location)
                    GPSConnnected = true
                    updateDeviceState(DeviceState.NoInternet)}
                else {
                    GPSConnnected = false
                    updateDeviceState(DeviceState.NoGPS)
                    requestUpdateLocation(activity)
                    Timber.d( "but it is null")
                }
            }
        }
    }

    override fun onProviderEnabled(provider: String) { }
    override fun onProviderDisabled(provider: String) {  }
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}