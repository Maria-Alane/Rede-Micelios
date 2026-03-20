package com.example.micelios.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemMomentoBinding
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.presentation.common.TimeFormatter

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MomentoViewHolder>() {

    private val items = mutableListOf<FeedMoment>()

    fun submitList(newItems: List<FeedMoment>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class MomentoViewHolder(
        private val binding: ItemMomentoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moment: FeedMoment) {
            val initials = moment.creatorDisplayName
                .trim()
                .split(" ")
                .take(2)
                .mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("")
                .uppercase()

            binding.textViewAvatar.text = initials
            binding.textViewAuthor.text = moment.creatorDisplayName
            binding.textViewCircleTime.text =
                "${moment.hyphaName} • ${TimeFormatter.formatElapsedTime(moment.timestamp)}"
            binding.textViewContent.text = moment.content
            binding.textViewAvatar.visibility = View.VISIBLE
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