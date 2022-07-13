package com.example.nickelfoxassignment.assignment0

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityGoogleMapsScreenBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class GoogleMapsScreen : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsScreenBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var marker: MarkerOptions
    private var polyline: Polyline? = null
    private var navigateToSettings = false
    private val latLngList: MutableList<LatLng> = mutableListOf()
    private var tamWorth = LatLng(46.392014, -117.010826)
    private var newCastle = LatLng(47.702465, -116.796883)
    private var brisbane = LatLng(47.538658, -116.129227)


    private val locationIntent =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            if (permissions.values.first()) {
                fetchLocation()
            } else {
                MaterialAlertDialogBuilder(this@GoogleMapsScreen)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setNegativeButton("Cancel") { _, _ ->
                    }
                    .show()
            }
        }

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Location required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleMapsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.homeMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        if (navigateToSettings) {
            navigateToSettings = false
            fetchLocation()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.bottom_out, R.anim.top_in)
        super.onBackPressed()
    }

    //Fetch user current location
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isLocationEnabled()) {
                val lr = LocationRequest.create().apply {
                    interval = 60000
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(lr, object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        super.onLocationResult(p0)
                        val location = p0.locations.last()
                        currentLocation = location
                        val latLng = LatLng(location.latitude, location.longitude)
                        drawMarker(latLng)
                    }
                }, null)
            } else {
                val locationRequest = LocationRequest.create()
                    .setInterval(10000)
                    .setFastestInterval(2000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)

                LocationServices
                    .getSettingsClient(this)
                    .checkLocationSettings(builder.build())
                    .addOnSuccessListener(this) { }
                    .addOnFailureListener(this) { ex ->
                        if (ex is ResolvableApiException) {
                            try {
                                val intentSenderRequest =
                                    IntentSenderRequest.Builder(ex.resolution)
                                        .build()
                                resolutionForResult.launch(intentSenderRequest)
                            } catch (exception: Exception) {
                                Log.d(TAG, "enableLocationSettings: $exception")
                            }
                        }
                    }
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                showDialog()
            } else {
                locationIntent.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "GoogleMapsScreen"
        private const val REQUEST_CODE = 100
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Enable Location")
            .setMessage("This app needs the Location permission, please turn on your location from app settings")
            .setPositiveButton("App Settings") { dialog, _ ->
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    data = uri
                    dialog.dismiss()
                    navigateToSettings = true
                    startActivity(this)
                }
            }
            .setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                fetchLocation()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        fetchLocation()
        setMapClick(mMap)
        drawPolyGon()
    }

    //Draw Marker on map
    private fun drawMarker(latLng: LatLng) {
        marker = MarkerOptions()
            .position(latLng)
            .title("Address")
            .snippet(getAddress(latLng.latitude, latLng.longitude))

        latLngList.add(latLng)
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    //Fetch address of the tapped location
    private fun getAddress(lat: Double, lan: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lan, 1)
        return addresses[0].getAddressLine(0).toString()
    }

    private fun setMapClick(map: GoogleMap) {
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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}