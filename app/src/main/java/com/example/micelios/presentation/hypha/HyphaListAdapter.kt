package com.example.micelios.presentation.hypha

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemHyphaBinding
import com.example.micelios.domain.model.Hypha

class HyphaListAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<HyphaListAdapter.HyphaViewHolder>() {

    private val items = mutableListOf<Hypha>()

    fun submitList(newItems: List<Hypha>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HyphaViewHolder(
        private val binding: ItemHyphaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hypha: Hypha) {
            binding.textViewHyphaBadge.text = hypha.name.take(2).uppercase()
            binding.textViewHyphaName.text = hypha.name
            binding.textViewHyphaMeta.text =
                if (hypha.description.isBlank()) {
                    hypha.type.name
                } else {
                    "${hypha.type.name} • ${hypha.description}"
                }

            binding.root.setOnClickListener { onClick(hypha.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HyphaViewHolder {
        val binding = ItemHyphaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HyphaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HyphaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}