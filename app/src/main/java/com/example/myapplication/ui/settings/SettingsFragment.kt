package com.example.myapplication.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.LoginActivity
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        loadUserProfile()

        binding.manageContactsButton.setOnClickListener {
            // TODO: Implement navigation to a new screen or dialog for managing emergency contacts
            Toast.makeText(context, "Manage Emergency Contacts (Placeholder)", Toast.LENGTH_SHORT).show()
        }

        binding.aiMonitoringSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO: Implement logic to enable AI monitoring features
                Toast.makeText(context, "AI Monitoring Enabled (Placeholder)", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Implement logic to disable AI monitoring features
                Toast.makeText(context, "AI Monitoring Disabled (Placeholder)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            // Navigate back to LoginActivity
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            binding.userNameEditText.setText(user.displayName ?: "Not Set")
            binding.userEmailEditText.setText(user.email ?: "Not Set")
            // You might want to allow users to update their display name.
            // binding.userNameEditText.isEnabled = true 
        } else {
            // This case should ideally not happen if the user is logged in and lands here.
            // Handle appropriately, e.g., navigate to login.
            binding.userNameEditText.setText("Error loading user")
            binding.userEmailEditText.setText("Error loading email")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
