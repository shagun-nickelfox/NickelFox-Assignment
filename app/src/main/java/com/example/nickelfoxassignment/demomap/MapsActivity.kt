package com.example.nickelfoxassignment.demomap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager


class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private var users: ArrayList<User> = ArrayList()
    private lateinit var clusterManager: ClusterManager<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            setUpClusterManager(googleMap)
        }
    }

    private fun setUpClusterManager(mMap: GoogleMap) {
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        clusterManager = ClusterManager<User>(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager)

        users = getUsers()

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(users.toList()[2].latLng, 14.0f))
        clusterManager.addItems(users)
        clusterManager.cluster()
        clusterManager.setOnClusterClickListener {
            deCluster(mMap)
            return@setOnClusterClickListener true
        }
    }

    private fun deCluster(mMap: GoogleMap) {
        val builder = LatLngBounds.builder()
        for (item in getUsers()) {
            builder.include(item.position)
        }
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100)
        mMap.animateCamera(cameraUpdate)

    }

    private fun getUsers(): ArrayList<User> {
        val tamWorth = LatLng(46.392014, -117.010826)
        val newCastle = LatLng(47.702465, -116.796883)
        val brisbane = LatLng(47.538658, -116.129227)
        val loc = LatLng(47.53864, -116.129228)
        val kar = LatLng(47.538657, -115.129224)
        val cal = LatLng(47.538656, -117.129227)

        val arrayList: ArrayList<User> = ArrayList()
        val user1 = User("Location1", tamWorth)
        val user2 = User("Location2", newCastle)
        val user3 = User("Location3", brisbane)
        val user4 = User("Location3", loc)
        val user5 = User("Location3", kar)
        val user6 = User("Location3", cal)

        arrayList.add(user1)
        arrayList.add(user2)
        arrayList.add(user3)
        arrayList.add(user4)
        arrayList.add(user5)
        arrayList.add(user6)

        return arrayList
    }

}