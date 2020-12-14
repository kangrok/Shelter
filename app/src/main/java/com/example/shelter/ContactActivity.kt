package com.example.shelter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.model.*

class ContactActivity  : AppCompatActivity(),
    OnMapReadyCallback, OnMyLocationButtonClickListener,
    OnMyLocationClickListener, OnRequestPermissionsResultCallback {

    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(58.390472, 26.746289), 13f))
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(58.390472, 26.746289))
                .title("Varjupaik")
        )
        map = googleMap ?: return
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }
    private fun requestPermission(
        activity: AppCompatActivity, requestId: Int,
        permission: String
    ) {
        if (!shouldShowRequestPermissionRationale(activity, permission)) {
            requestPermissions(
                activity,
                arrayOf(permission),
                requestId
            )
        }
    }
}