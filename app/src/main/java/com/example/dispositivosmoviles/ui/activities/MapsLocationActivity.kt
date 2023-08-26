package com.example.dispositivosmoviles.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityMapsLocationBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient

class MapsLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsLocationBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var client: SettingsClient
    private lateinit var locationSettingRequest: LocationSettingsRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        )
            .setMaxUpdates(3)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult !== null) {
                    locationResult.locations.forEach { location ->
                        Log.d(
                            "UCE",
                            "Ubicacion: ${location.latitude}, " +
                                    "${location.longitude}"
                        )
                    }
                }
            }
        }

        client = LocationServices.getSettingsClient(this)
        locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
    }

    @SuppressLint("MissingPermission")
    private val locationContract =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    client.checkLocationSettings(locationSettingRequest).apply {
                        addOnSuccessListener {
                            val task = fusedLocationProviderClient.lastLocation
                            task.addOnSuccessListener { location ->
                                fusedLocationProviderClient.requestLocationUpdates(
                                    locationRequest,
                                    locationCallback,
                                    Looper.getMainLooper()
                                )
                            }

                        }
                        addOnFailureListener { ex ->
                            if (ex is ResolvableApiException) {
                                //Para que abra las configuraciones de idioma
                                //
                                //startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))

                                ex.startResolutionForResult(
                                    this@MapsLocationActivity,
                                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                                )

                            }

                        }
                    }

                }


                shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {

                }

                false -> {

                }

            }
        }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(
            locationCallback
        )
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        binding.btnStart.setOnClickListener {
            fusedLocationProviderClient.removeLocationUpdates(
                locationCallback
            )
            // Obtener la última ubicación conocida
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Crear la URL con las coordenadas
                    val mapsUrl = "http://maps.google.com/maps?q=$latitude,$longitude"

                    // Abrir Google Maps usando un intent
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                    intent.setPackage("com.google.android.apps.maps")
                    startActivity(intent)
                }
            }
        }
    }
}