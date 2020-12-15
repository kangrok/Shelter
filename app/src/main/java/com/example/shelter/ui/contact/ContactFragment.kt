package com.example.shelter.ui.contact

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.shelter.R
import com.example.shelter.utils.LocationHelper
import com.example.shelter.utils.ViewWeightAnimationWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.JsonObject
import com.google.maps.android.PolyUtil
import com.koushikdutta.ion.Ion


class ContactFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var mMapContainer: RelativeLayout
    private lateinit var mLocationDescription: ScrollView
    private lateinit var mFullscreenButton: ImageButton
    private val shelterLoc = LatLng(58.390510, 26.746015)
    private val bundleKey = "mapBundleKey"
    private var mapExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        mMapView = view.findViewById(R.id.mapView)
        mMapContainer = view.findViewById(R.id.map_container)
        mLocationDescription = view.findViewById(R.id.location_description)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(bundleKey)
        }

        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)

        mFullscreenButton = view.findViewById(R.id.button_map_full)
        mFullscreenButton.setOnClickListener { toggleFullscreen() }

        return view
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) mMap = map
        if (permissionGranted()) {
            loadPath()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
    }

    private fun loadLocation() {
        mMap.addMarker(MarkerOptions().position(shelterLoc).title("Shelter"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shelterLoc, 12F))
    }

    @SuppressLint("MissingPermission")
    private fun loadPath() {
        val helper = LocationHelper(requireActivity().applicationContext)
        if (!helper.isGPSEnabled) {
            loadLocation()
            return
        }
        val currentLoc = helper.getCurrentLocationUsingGPS()

        if (currentLoc != null) {
            mMap.isMyLocationEnabled = true
            val currentLatlng = LatLng(currentLoc.latitude, currentLoc.longitude)
            displayPath(currentLatlng, shelterLoc)
        }
    }

    private fun displayPath(start: LatLng, dest: LatLng) {
        Ion.with(this)
            .load("https://maps.googleapis.com/maps/api/directions/json")
            .addQuery("origin", "${start.latitude},${start.longitude}")
            .addQuery("destination", "${dest.latitude},${dest.longitude}")
            .addQuery("mode", "driving")
            .addQuery("key", resources.getString(R.string.google_maps_key))
            .asJsonObject()
            .setCallback { _, result ->
                val routes = result.asJsonObject.getAsJsonArray("routes")

                if (routes.size() > 0) {
                    val route = routes[0].asJsonObject.getAsJsonArray("legs")[0].asJsonObject
                    val time = route.getAsJsonObject("duration").get("text").asString
                    val distance = route.getAsJsonObject("distance").get("text").asString

                    mMap.addMarker(
                        MarkerOptions()
                            .position(start)
                            .title("Your Location")
                    )
                    mMap.addMarker(
                        MarkerOptions()
                            .position(dest)
                            .title("Shelter")
                            .snippet("$time ($distance)")
                    )
                    drawPolyline(route, dest)
                }
            }
    }

    private fun drawPolyline(route: JsonObject, dest: LatLng) {
        val polylineOptions = PolylineOptions()
        val builder = LatLngBounds.Builder()
        route.getAsJsonArray("steps").forEach { step ->
            val latLngJson = step.asJsonObject.get("start_location").asJsonObject
            val latLng = LatLng(latLngJson.get("lat").asDouble, latLngJson.get("lng").asDouble)
            builder.include(latLng)

            val polyLinePoints = step.asJsonObject.get("polyline").asJsonObject.get("points").asString
            val decoded = PolyUtil.decode(polyLinePoints)
            polylineOptions.addAll(decoded)
        }
        polylineOptions.add(dest)
        polylineOptions.jointType(JointType.ROUND)
        polylineOptions.color(Color.DKGRAY)
        mMap.addPolyline(polylineOptions)

        val bounds = builder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(bundleKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(bundleKey, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    private fun permissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionGranted()) {
            loadPath()
        } else {
            loadLocation()
        }
    }

    private fun toggleFullscreen() {
        mapExpanded = if (mapExpanded) {
            contractMapAnimation()
            mFullscreenButton.setImageResource(R.drawable.ic_baseline_fullscreen_24)
            false
        } else {
            expandMapAnimation()
            mFullscreenButton.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24)
            true
        }
    }

    private fun expandMapAnimation() {
        val mapAnimationWrapper = ViewWeightAnimationWrapper(mMapContainer)
        val mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper, "weight", 1f, 1f)
        mapAnimation.duration = 800
        val infoAnimationWrapper = ViewWeightAnimationWrapper(mLocationDescription)
        val infoAnimation = ObjectAnimator.ofFloat(infoAnimationWrapper, "weight", 1f, 0f)
        infoAnimation.duration = 800
        infoAnimation.start()
        mapAnimation.start()
    }

    private fun contractMapAnimation() {
        val mapAnimationWrapper = ViewWeightAnimationWrapper(mMapContainer)
        val mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper, "weight", 1f, 1f)
        mapAnimation.duration = 800
        val infoAnimationWrapper = ViewWeightAnimationWrapper(mLocationDescription)
        val infoAnimation = ObjectAnimator.ofFloat(infoAnimationWrapper, "weight", 0f, 1f)
        infoAnimation.duration = 800
        infoAnimation.start()
        mapAnimation.start()
    }
}