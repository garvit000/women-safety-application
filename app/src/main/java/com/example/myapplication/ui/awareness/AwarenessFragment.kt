package com.example.myapplication.ui.awareness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentAwarenessBinding

class AwarenessFragment : Fragment() {

    private var _binding: FragmentAwarenessBinding? = null
    private val binding get() = _binding!!

    private lateinit var awarenessAdapter: AwarenessAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAwarenessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadAwarenessData()
    }

    private fun setupRecyclerView() {
        awarenessAdapter = AwarenessAdapter(emptyList()) // Initialize with an empty list
        binding.awarenessRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = awarenessAdapter
        }
    }

    private fun loadAwarenessData() {
        // Dummy data for prototype
        val dummyData = listOf(
            AwarenessItem("Stay Alert in Crowded Places", "Be mindful of your surroundings and belongings in crowded areas. Pickpocketing can be common.", ItemType.TIP),
            AwarenessItem("Share Your Travel Plans", "Before going out, especially at night, share your live location or travel itinerary with a trusted friend or family member.", ItemType.TIP),
            AwarenessItem("Avoid Unlit Shortcuts", "Stick to well-lit paths and main roads, especially when walking alone at night. Avoid dark alleys or isolated shortcuts.", ItemType.GUIDELINE),
            AwarenessItem("Community Alert: Sector 15", "A recent increase in chain snatching incidents has been reported in Sector 15. Please be extra cautious.", ItemType.ALERT),
            AwarenessItem("Trust Your Instincts", "If a situation or person makes you feel uncomfortable, remove yourself from the situation immediately. Don\'t worry about being polite.", ItemType.TIP)
        )
        awarenessAdapter = AwarenessAdapter(dummyData)
        binding.awarenessRecyclerView.adapter = awarenessAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
