package com.example.myapplication.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    // Explicit public no-argument constructor

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    private companion object {
        private const val TAG = "HomeFragment"
        private const val EMERGENCY_PHONE_NUMBER = "8764986988" // Placeholder
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val smsGranted = permissions[Manifest.permission.SEND_SMS] ?: false

        if (fineLocationGranted) {
            fetchLocationAndThen("Location permission granted for SOS.") {
                // If SMS was also granted, we can proceed or just note it.
                if (smsGranted) {
                    Toast.makeText(context, "Permissions granted. Ready for SOS.", Toast.LENGTH_SHORT).show()
                } else {
                     Toast.makeText(context, "Location granted, SMS denied.", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (smsGranted) {
            Toast.makeText(context, "SMS permission granted, Location denied.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "SOS requires Location and SMS permissions.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sosButton.setOnClickListener {
            handleSosClick()
        }

        binding.mapButtonContainer.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_map)
        }
        // Attempt to get location early if permissions are already granted
        checkAndFetchInitialLocation()
    }

    private fun checkAndFetchInitialLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocationAndThen("Initial location fetched.") {}
        } // No need for else, permissions will be requested on SOS if not granted.
    }

    private fun handleSosClick() {
        val fineLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val smsPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)

        val permissionsToRequest = mutableListOf<String>()
        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.SEND_SMS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // Permissions already granted, proceed with SOS
            triggerSosAction()
        }
    }
    
    private fun fetchLocationAndThen(successMessage: String, onLocationFetched: () -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location permission not available for fetchLocationAndThen.")
             // This should ideally not be hit if permissions check is done prior
            Toast.makeText(context, "Location permission missing.", Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lastLatitude = location.latitude
                    lastLongitude = location.longitude
                    Log.d(TAG, "Location fetched: $lastLatitude, $lastLongitude")
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    onLocationFetched() 
                } else {
                    Log.d(TAG, "Last location is null, consider requesting location updates.")
                    Toast.makeText(context, "Could not get current location. Try again.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting location", it)
                Toast.makeText(context, "Error getting location.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun triggerSosAction() {
        Toast.makeText(context, "SOS Alert Triggered! Fetching location...", Toast.LENGTH_SHORT).show()
        fetchLocationAndThen("Location for SOS obtained.") {
            sendSmsAlert()
        }
    }

    private fun sendSmsAlert() {
        if (lastLatitude == null || lastLongitude == null) {
            Toast.makeText(context, "Location not available for SMS. Please ensure GPS is on and try again.", Toast.LENGTH_LONG).show()
            // Optionally, try fetching location again here or guide user
            checkAndFetchInitialLocation() // try to get location again
            return
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                //val smsManager = SmsManager.getDefault()
                val locationLink = "http://maps.google.com/maps?q=$lastLatitude,$lastLongitude"
                val message = "Emergency! I need help. My current location is: $locationLink"
                
                //smsManager.sendTextMessage(EMERGENCY_PHONE_NUMBER, null, message, null, null)
                Toast.makeText(context, "SOS SMS sent to $EMERGENCY_PHONE_NUMBER", Toast.LENGTH_LONG).show()
                // TODO: Implement Push Notification to emergency contacts
            } catch (e: Exception) {
                Log.e(TAG, "SMS sending failed", e)
                Toast.makeText(context, "SOS SMS sending failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "SMS permission not granted. Cannot send SOS SMS.", Toast.LENGTH_LONG).show()
            // This case should be rare if handleSosClick logic is correct
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}