package com.owl.noteowl.features.addNote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ItemLabelAddNoteBinding
import com.owl.noteowl.utils.Constants
import com.owl.noteowl.utils.ContextUtility
import io.realm.OrderedRealmCollection
import io.realm.RealmList

class AddLabelAdapter(
    val context: Context,
    val labels: RealmList<Label>,
    val viewModel: AddNoteViewModel,
    val selectLabel: () -> Unit
) : RecyclerView.Adapter<AddLabelAdapter.AddLabelHolder>() {

    private val contextUtility by lazy {
        ContextUtility(context)
    }
    private val POSITION by lazy { Constants.Position() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLabelHolder {
        return AddLabelHolder(
            ItemLabelAddNoteBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: AddLabelHolder, position: Int) {
        labels[position]?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return POSITION.FIRST
        else if (position == labels.size - 1)
            return POSITION.LAST
        return POSITION.MID
    }

    fun update(labels: OrderedRealmCollection<Label>) {
        val diffUtil = DiffUtil.calculateDiff(LabelListDiff(labels))
        this.labels.apply {
            clear()
            addAll(labels)
        }
        diffUtil.dispatchUpdatesTo(this)
    }

    inner class AddLabelHolder(val binding: ItemLabelAddNoteBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        fun bind(label: Label) {
            binding.viewType = getItemViewType(adapterPosition)
            binding.label = label
            binding.utility = contextUtility

            //click listeners
            binding.imvCloseLabel.setOnClickListener(this)
            binding.root.setOnClickListener(this)

            //rotating cross to plus
            binding.imvCloseLabel.rotation = if (adapterPosition == 0) 45f else 0f
        }

        //click callback
        override fun onClick(view: View?) {
            when (view?.id) {
                //remove label
                R.id.imv_close_label -> {
                    if (adapterPosition > 0) {
                        viewModel.removeLabel(adapterPosition)
                    } else {
                        selectLabel()
                    }
                }

                //add label
                else -> {
                    if (adapterPosition == 0) {
                        selectLabel()
                    }
                }
            }
        }
    }

    //Labels list Difference callback
    inner class LabelListDiff(val newList: List<Label>) : DiffUtil.Callback() {
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition].createdAt == labels[oldItemPosition]?.createdAt
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun getOldListSize(): Int {
            return labels.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == labels[oldItemPosition]
        }
    }
}