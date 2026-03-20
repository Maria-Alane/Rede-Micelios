package com.example.micelios.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemFeedEndBinding
import com.example.micelios.databinding.ItemMomentoBinding
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.presentation.common.TimeFormatter

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<FeedMoment>()

    fun submitList(newItems: List<FeedMoment>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) {
            VIEW_TYPE_MOMENT
        } else {
            VIEW_TYPE_END
        }
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

    inner class FeedEndViewHolder(
        private val binding: ItemFeedEndBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() = Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MOMENT -> {
                val binding = ItemMomentoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MomentoViewHolder(binding)
            }

            else -> {
                val binding = ItemFeedEndBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FeedEndViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MomentoViewHolder -> holder.bind(items[position])
            is FeedEndViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 0 else items.size + 1
    }

    companion object {
        private const val VIEW_TYPE_MOMENT = 1
        private const val VIEW_TYPE_END = 2
    }
}