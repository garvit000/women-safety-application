package com.example.myapplication.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private companion object {
        private const val TAG = "MapFragment"
        private const val DEFAULT_ZOOM = 15f
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineLocationGranted || coarseLocationGranted) {
            enableMyLocation()
            getDeviceLocation()
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view_container) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                enableMyLocation()
                getDeviceLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                //  Explain to the user why you need the permission
                Toast.makeText(context, "Location permission is needed to show your current location on the map.", Toast.LENGTH_LONG).show()
                requestLocationPermissions()
            }
            else -> {
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions(){
        requestPermissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            Log.d(TAG, "Location permission not granted for enableMyLocation")
        }
    }

    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val currentLatLng = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.")
                            // Use a default location if current location is not available
                            // For example, Googleplex
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.4220, -122.0841), DEFAULT_ZOOM))
                            googleMap?.uiSettings?.isMyLocationButtonEnabled = false // No current location, so disable button
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Task failed.")
                        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.4220, -122.0841), DEFAULT_ZOOM))
                        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        googleMap = null // Clean up the map
    }
}
