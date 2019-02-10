package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.databinding.ItemNoteBinding
import com.owl.noteowl.extensions.text

class NotesAdapter(
    val context: Context,
    val notes: ArrayList<Note>,
    val onNoteClicked: (note: Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    //updating list
    fun update(newNotes: List<Note>) {
        val noteDiff = DiffUtil.calculateDiff(NoteDiffUtil(newNotes))
        notes.clear()
        notes.addAll(newNotes)
        noteDiff.dispatchUpdatesTo(this)
    }

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(note: Note) {
            binding.note = note
            binding.rvLabelsNote.adapter = LabelsInNoteAdapter(context, note.labels ?: emptyList())
            binding.tvNoteDate.text = note.createdAt.text("dd MMM")
            binding.imvNoteBanner.clipToOutline = true
        }

        override fun onClick(v: View?) {
            onNoteClicked(notes[adapterPosition])
        }
    }

    inner class NoteDiffUtil(private val newNotes: List<Note>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return notes[oldItemPosition].id == newNotes[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return notes.size
        }

        override fun getNewListSize(): Int {
            return newNotes.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return notes[oldItemPosition] == newNotes[newItemPosition]
        }

    }
}
