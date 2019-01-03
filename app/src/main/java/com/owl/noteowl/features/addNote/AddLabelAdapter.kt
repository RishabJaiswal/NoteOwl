package com.owl.noteowl.features.addNote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelAddNoteBinding

class AddLabelAdapter(val context: Context, val labels: List<Label>) :
    RecyclerView.Adapter<AddLabelAdapter.AddLabelHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLabelHolder {
        return AddLabelHolder(
            ItemLabelAddNoteBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size + 1
    }

    override fun onBindViewHolder(holder: AddLabelHolder, position: Int) {
        if (position == 0) {
            holder.bind(Label().apply {
                title = context.getString(R.string.add_label)
                colorHex = "#c6c6c6"
            })
        } else {
            holder.bind(labels[position])
        }
    }

    inner class AddLabelHolder(val binding: ItemLabelAddNoteBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        fun bind(label: Label) {
            binding.label = label
            binding.imvCloseLabel.setOnClickListener(this)
            binding.root.setOnClickListener(this)

            //rotating cross to plus
            binding.imvCloseLabel.rotation = if (adapterPosition == 0) 45f else 0f
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                //remove label
                R.id.imv_close_label -> {
                    if (adapterPosition > 0) {
                        //todo:: remove label from list
                    }
                }

                //add label
                else -> {
                    if (adapterPosition == 0) {
                        //todo:: add label to list
                    }
                }
            }
        }
    }
}