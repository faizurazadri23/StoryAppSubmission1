package com.faizurazadri.storyappsubmission1.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityMapsBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var userData: LoginResult

    companion object {
        private const val TAG = "Maps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)


        userData =
            Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        userData.token?.let { storyViewModel.getStoriesLocation(it, 1) }

        storyViewModel.storyList.observe(this) {

            for (item in it){

                val location = item.lat?.let { it1 -> item.lon?.let { it2 -> LatLng(it1, it2) } }

                location?.let { it1 -> MarkerOptions().position(it1).title(item.name) }
                    ?.let { it2 -> mMap.addMarker(it2) }
            }

            val focusLocation = it.get(0).lon?.let { it1 -> it[0].lat?.let { it2 -> LatLng(it2, it1) } }
            focusLocation?.let { it1 -> CameraUpdateFactory.newLatLng(it1) }
                ?.let { it2 -> mMap.moveCamera(it2) }
        }

        // Add a marker in Sydney and move the camera




        getMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }

            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}