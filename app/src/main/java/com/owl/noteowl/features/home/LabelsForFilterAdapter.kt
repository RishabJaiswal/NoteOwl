package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelFilterNotesBinding
import com.owl.noteowl.utils.visibleOrGone
import kotlinx.android.synthetic.main.item_label_filter_notes.view.*
import java.util.*

class LabelsForFilterAdapter(val context: Context, val viewModel: NotesViewModel) :
    RecyclerView.Adapter<LabelsForFilterAdapter.LabelViewHolder>() {

    val labels: ArrayList<Label> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelsForFilterAdapter.LabelViewHolder {
        return LabelViewHolder(
            ItemLabelFilterNotesBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: LabelsForFilterAdapter.LabelViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    fun update(newLabels: List<Label>) {
        val diffUtil = DiffUtil.calculateDiff(LabelDiffUtil(newLabels))
        this.labels.clear()
        this.labels.addAll(newLabels)
        diffUtil.dispatchUpdatesTo(this)
    }

    fun clearFilter() {
        notifyDataSetChanged()
    }

    //label view holder
    inner class LabelViewHolder(private val binding: ItemLabelFilterNotesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.labelParent.setOnClickListener(this)
            binding.labelParentBg.setOnClickListener(this)
        }

        fun bind(label: Label) {
            binding.label = label
            showFilteredLabel()
            binding.executePendingBindings()
        }

        override fun onClick(view: View?) {
            binding.label?.let { label ->
                EditLabelDialog(context, label.id, viewModel).show()
            }
        }

        fun showFilteredLabel() {
            val isLabelInFilter = viewModel.containsLabelInFilter(binding.label?.title ?: "")
            visibleOrGone(binding.imvSelected, isLabelInFilter)
        }

        fun getSwipeableView(): View {
            return itemView.label_parent
        }

        fun filterNote() {
            viewModel.editFilter(binding.label?.title)
            showFilteredLabel()
        }
    }

    inner class LabelDiffUtil(val newLabels: List<Label>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return labels[oldItemPosition].id == newLabels[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return labels.size
        }

        override fun getNewListSize(): Int {
            return newLabels.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return labels[oldItemPosition] == newLabels[newItemPosition]
        }
    }
}