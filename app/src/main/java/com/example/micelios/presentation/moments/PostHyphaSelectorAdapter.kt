package com.example.micelios.presentation.moments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemHyphaOptionBinding
import com.example.micelios.domain.model.Hypha

class PostHyphaSelectorAdapter(
    private val onSelected: (Long) -> Unit
) : RecyclerView.Adapter<PostHyphaSelectorAdapter.HyphaViewHolder>() {

    private val items = mutableListOf<Hypha>()
    private var selectedHyphaId: Long? = null

    fun submitList(newItems: List<Hypha>) {
        items.clear()
        items.addAll(newItems)

        if (selectedHyphaId != null && items.none { it.id == selectedHyphaId }) {
            selectedHyphaId = null
        }

        notifyDataSetChanged()
    }

    inner class HyphaViewHolder(
        private val binding: ItemHyphaOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hypha: Hypha) {
            val isSelected = hypha.id == selectedHyphaId

            binding.textViewHyphaName.text = hypha.name
            binding.textViewHyphaMeta.text =
                if (hypha.description.isBlank()) {
                    hypha.type.name
                } else {
                    "${hypha.type.name} • ${hypha.description}"
                }

            binding.radioSelected.isChecked = isSelected

            binding.root.setOnClickListener {
                val previousSelectedId = selectedHyphaId
                selectedHyphaId = hypha.id
                onSelected(hypha.id)

                val previousIndex = items.indexOfFirst { it.id == previousSelectedId }
                val currentIndex = bindingAdapterPosition

                if (previousIndex != -1) {
                    notifyItemChanged(previousIndex)
                }
                if (currentIndex != RecyclerView.NO_POSITION) {
                    notifyItemChanged(currentIndex)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HyphaViewHolder {
        val binding = ItemHyphaOptionBinding.inflate(
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