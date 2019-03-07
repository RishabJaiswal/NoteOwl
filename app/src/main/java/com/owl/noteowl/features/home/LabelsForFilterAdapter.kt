package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelFilterNotesBinding
import com.owl.noteowl.utils.Constants
import com.owl.noteowl.utils.ContextUtility
import java.util.*

class LabelsForFilterAdapter(
    val context: Context,
    val viewModel: NotesViewModel,
    val onLabelClicked: (label: Label?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val labels: ArrayList<Label> = arrayListOf()
    private val POSITION = Constants.Position()
    private val labelTranslationY by lazy {
        ContextUtility(context).convertDpToPx(8f)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LabelViewHolder(
            ItemLabelFilterNotesBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LabelViewHolder) {
            holder.bind(labels[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return POSITION.FIRST
        return POSITION.MID
    }

    fun update(labels: List<Label>) {
        val diffUtil = DiffUtil.calculateDiff(LabelDiffUtil(labels))
        this.labels.clear()
        this.labels.addAll(labels)
        diffUtil.dispatchUpdatesTo(this)
    }

    fun clearFilter() {
        notifyDataSetChanged()
    }

    //label view holder
    inner class LabelViewHolder(private val binding: ItemLabelFilterNotesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(label: Label) {
            binding.label = label
            showFilteredLabel()
            binding.executePendingBindings()
        }

        override fun onClick(view: View?) {
            onLabelClicked(binding.label)
            //animating label
            showFilteredLabel()
        }

        fun showFilteredLabel() {
            /*val isLabelInFilter = viewModel.containsLabelInFilter(binding.label?.title ?: "")
            itemView.animate().translationY(
                if (isLabelInFilter) {
                    0f
                } else {
                    labelTranslationY
                }
            ).start()*/
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

    inner class LabelDiffUtil(val newLabels: List<Label>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return labels[oldItemPosition].title == newLabels[newItemPosition].title
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