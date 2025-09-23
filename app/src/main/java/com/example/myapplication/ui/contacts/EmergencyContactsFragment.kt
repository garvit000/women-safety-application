
package com.example.myapplication.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentEmergencyContactsBinding
// import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.database.DatabaseReference
// import com.google.firebase.database.FirebaseDatabase

class EmergencyContactsFragment : Fragment() {

    private var _binding: FragmentEmergencyContactsBinding? = null
    private val binding get() = _binding!!

    // private lateinit var auth: FirebaseAuth
    // private lateinit var database: DatabaseReference
    // private lateinit var contactsAdapter: EmergencyContactAdapter
    // private val contactsList = mutableListOf<EmergencyContact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // auth = FirebaseAuth.getInstance()
        // val userId = auth.currentUser?.uid
        // if (userId == null) {
        //     // Handle user not logged in, perhaps navigate back or show error
        //     findNavController().popBackStack()
        //     return
        // }
        // database = FirebaseDatabase.getInstance().getReference("emergency_contacts").child(userId)

        // setupRecyclerView()
        // loadContacts()

        binding.addContactFab.setOnClickListener {
            // Navigate to an Add/Edit Contact Fragment/Dialog (to be created)
            // For now, we can log or show a Toast
            // findNavController().navigate(R.id.action_nav_emergency_contacts_to_addEditContactFragment)
            android.widget.Toast.makeText(context, "Add contact clicked (not implemented yet)", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // private fun setupRecyclerView() { // ... Implementation later ... }
    // private fun loadContacts() { // ... Implementation later ... }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
