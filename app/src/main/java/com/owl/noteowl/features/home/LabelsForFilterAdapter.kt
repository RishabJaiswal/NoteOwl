package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelHomeFiltersBinding

class LabelsForFilterAdapter(
    val context: Context,
    val labels: List<Label>,
    val onLabelClicked: (label: Label?) -> Unit
) :
    RecyclerView.Adapter<LabelsForFilterAdapter.LabelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        return LabelViewHolder(
            ItemLabelHomeFiltersBinding.inflate(
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

    inner class LabelViewHolder(private val binding: ItemLabelHomeFiltersBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(label: Label) {
            binding.label = label
        }

        override fun onClick(view: View?) {
            var label: Label? = null
            if (adapterPosition != 0) {
                label = labels[adapterPosition]
            }
            onLabelClicked(label)
        }
    }
}