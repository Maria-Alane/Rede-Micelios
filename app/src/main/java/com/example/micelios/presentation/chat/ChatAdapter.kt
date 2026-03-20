package com.example.micelios.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemChatIncomingBinding
import com.example.micelios.databinding.ItemChatOutgoingBinding
import com.example.micelios.domain.model.Message
import com.example.micelios.presentation.common.TimeFormatter

class ChatAdapter(
    private val currentUserId: Long?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Message>()

    fun submitList(newItems: List<Message>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val message = items[position]
        return if (message.senderUserId == currentUserId) VIEW_TYPE_OUTGOING else VIEW_TYPE_INCOMING
    }

    inner class IncomingViewHolder(
        private val binding: ItemChatIncomingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.textViewSender.text = message.senderDisplayName
            binding.textViewMessageContent.text = message.content
            binding.textViewMessageTime.text = TimeFormatter.formatElapsedTime(message.timestamp)
        }
    }

    inner class OutgoingViewHolder(
        private val binding: ItemChatOutgoingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.textViewSender.text = message.senderDisplayName
            binding.textViewMessageContent.text = message.content
            binding.textViewMessageTime.text = TimeFormatter.formatElapsedTime(message.timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_OUTGOING) {
            val binding = ItemChatOutgoingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OutgoingViewHolder(binding)
        } else {
            val binding = ItemChatIncomingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            IncomingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = items[position]
        when (holder) {
            is IncomingViewHolder -> holder.bind(message)
            is OutgoingViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = items.size

    companion object {
        private const val VIEW_TYPE_INCOMING = 1
        private const val VIEW_TYPE_OUTGOING = 2
    }
}