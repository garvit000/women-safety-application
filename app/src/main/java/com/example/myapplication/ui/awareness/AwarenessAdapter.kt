package com.example.myapplication.ui.awareness

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemAwarenessContentBinding

class AwarenessAdapter(private val awarenessItems: List<AwarenessItem>) :
    RecyclerView.Adapter<AwarenessAdapter.AwarenessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwarenessViewHolder {
        val binding = ItemAwarenessContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AwarenessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AwarenessViewHolder, position: Int) {
        val item = awarenessItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = awarenessItems.size

    class AwarenessViewHolder(private val binding: ItemAwarenessContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AwarenessItem) {
            binding.itemAwarenessTitle.text = item.title
            binding.itemAwarenessContent.text = item.content
            // You could add logic here to change icon or style based on item.type
        }
    }
}