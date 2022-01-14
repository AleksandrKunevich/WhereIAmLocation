package com.example.whereiamlocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.whereiamlocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager

    private val locationListener: LocationListener = LocationListener { location ->
        binding.textviewCoordinate.text = "Your location :${
            String.format(
                "%1$.4f, %2$.4f",
                location.latitude,
                location.longitude
            )
        }"
        binding.buttonWhereIAm.setOnClickListener {
            val intent: Intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "geo:${location.latitude},${location.longitude}"
                )
            )
            startActivity(Intent.createChooser(intent, "Let`s see in Map where I am."))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.buttonLocationSettings.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        }

    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.textviewCoordinate.text = getString(R.string.enableGPSplease)
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 1110L, 0f, locationListener
        )

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1110L, 0f, locationListener
        )
    }
}