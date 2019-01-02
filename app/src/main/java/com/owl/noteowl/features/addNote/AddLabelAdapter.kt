package com.owl.noteowl.features.addNote

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

            //rotating cross to plus
            if (adapterPosition == 0) {
                binding.imvCloseLabel.rotation = 45f
            } else {
                binding.imvCloseLabel.rotation = 0f
                Color.BLACK
            }
        }

        override fun onClick(view: View?) {
            if (adapterPosition == 0) {
                Toast.makeText(context, "Label added", Toast.LENGTH_LONG).show()
            }
        }
    }
}