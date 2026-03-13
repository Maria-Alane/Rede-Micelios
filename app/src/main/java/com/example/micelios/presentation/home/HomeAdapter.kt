package com.example.micelios.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemMomentoBinding
import com.example.micelios.domain.model.Moment
import com.example.micelios.presentation.common.TimeFormatter

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MomentoViewHolder>() {

    private val items = mutableListOf<Moment>()

    fun submitList(newItems: List<Moment>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class MomentoViewHolder(
        private val binding: ItemMomentoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moment: Moment) {
            binding.textViewAvatar.text = moment.creatorName.take(2).uppercase()
            binding.textViewAuthor.text = moment.creatorName
            binding.textViewCircleTime.text =
                "Hypha ${moment.hyphaId} • ${TimeFormatter.formatElapsedTime(moment.timestamp)}"
            binding.textViewContent.text = moment.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentoViewHolder {
        val binding = ItemMomentoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MomentoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MomentoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}