package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelHomeFiltersBinding
import com.owl.noteowl.utils.Constants

class LabelsForFilterAdapter(
    val context: Context,
    val labels: List<Label>,
    val onLabelClicked: (label: Label?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POSITION = Constants.Position()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == POSITION.FIRST) {
            return AddNoteViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_add_note_label_filters,
                    null,
                    false
                )
            )
        }
        return LabelViewHolder(
            ItemLabelHomeFiltersBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LabelViewHolder) {
            holder.bind(labels[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return POSITION.FIRST
        return super.getItemViewType(position)
    }


    //label view holder
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

    //add note viewholder
    inner class AddNoteViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onLabelClicked(null)
        }
    }
}