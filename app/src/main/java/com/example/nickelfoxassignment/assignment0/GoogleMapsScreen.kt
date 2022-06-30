package com.example.nickelfoxassignment.assignment0

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityGoogleMapsScreenBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class GoogleMapsScreen : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsScreenBinding
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentLocation: Location? = null
    private var polyline: Polyline? = null
    private val latLngList: MutableList<LatLng> = mutableListOf()
    private var tamWorth = LatLng(46.392014, -117.010826)
    private var newCastle = LatLng(47.702465, -116.796883)
    private var brisbane = LatLng(47.538658, -116.129227)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleMapsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    override fun onBackPressed() {
        finish();
        overridePendingTransition(R.anim.bottom_out, R.anim.top_in)
        super.onBackPressed()
    }

    //Fetch user current location
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
            return
        }

        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null) {
                this.currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.homeMap) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        drawMarker(latLng)
        setMapLongClick(mMap)
        drawPolyGon()
    }

    //Draw Marker on map
    private fun drawMarker(latLng: LatLng) {
        val marker = MarkerOptions()
            .position(latLng)
            .title("Your Location")
            .snippet(getAddress(latLng.latitude, latLng.longitude))

        latLngList.add(latLng)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        mMap.addMarker(marker)

    }

    //Fetch address of the tapped location
    private fun getAddress(lat: Double, lan: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lan, 1)
        return addresses[0].getAddressLine(0).toString()
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapClickListener { latLng ->
            drawMarker(latLng)
            drawPolyLine(latLng)
        }
    }

    //Draw Polygon
    private fun drawPolyGon() {
        val polygon = mMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(brisbane, newCastle, tamWorth, brisbane)
        )
        polygon.tag = "beta"
        val setToolbarTitle = CustomPolygon()
        setToolbarTitle.stylePolygon(polygon)
    }

    //Draw PolyLine
    private fun drawPolyLine(latLng: LatLng) {
        latLngList.add(latLng)
        polyline?.remove()
        val polyLine = PolylineOptions().addAll(latLngList)
            .clickable(true)
            .width(5f)
            .color(R.color.black)
        mMap.addPolyline(polyLine)
    }
}