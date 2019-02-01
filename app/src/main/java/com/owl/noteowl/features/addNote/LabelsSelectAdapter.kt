package com.owl.noteowl.features.addNote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelHomeFiltersBinding

class LabelsSelectAdapter(
    val context: Context,
    val labels: List<Label>,
    val onLabelClicked: (label: Label?) -> Unit
) : RecyclerView.Adapter<LabelsSelectAdapter.LabelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        return LabelViewHolder(
            ItemLabelHomeFiltersBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LabelsSelectAdapter.LabelViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    inner class LabelViewHolder(private val binding: ItemLabelHomeFiltersBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(label: Label) {
            binding.label = label
        }

        override fun onClick(view: View?) {
            if (adapterPosition >= 0 && adapterPosition < labels.size) {
                onLabelClicked(labels[adapterPosition])
            }
        }
    }
}