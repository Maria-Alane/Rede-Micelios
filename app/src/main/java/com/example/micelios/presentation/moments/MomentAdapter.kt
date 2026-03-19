package com.example.micelios.presentation.moments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.micelios.databinding.ItemMomentoBinding
import com.example.micelios.domain.model.Moment

class MomentAdapter(
    private val momentos: List<Moment>
) : RecyclerView.Adapter<MomentAdapter.MomentoViewHolder>() {

    inner class MomentoViewHolder(val binding: ItemMomentoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(moment: Moment) {
            binding.textViewAvatar.text = moment.creatorName.take(2).uppercase()
            binding.textViewAuthor.text = moment.creatorName
            binding.textViewCircleTime.text = "Hypha ${moment.hyphaId} • há minutos" // Pode formatar
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
        holder.bind(momentos[position])
    }

    override fun getItemCount(): Int = momentos.size
}