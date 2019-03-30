package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelNoteHomeBinding

class LabelsInNoteAdapter(
    val context: Context,
    val labels: List<Label>
) : RecyclerView.Adapter<LabelsInNoteAdapter.LabelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        return LabelViewHolder(
            ItemLabelNoteHomeBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    inner class LabelViewHolder(private val binding: ItemLabelNoteHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(label: Label) {
            binding.label = label
        }
    }
}