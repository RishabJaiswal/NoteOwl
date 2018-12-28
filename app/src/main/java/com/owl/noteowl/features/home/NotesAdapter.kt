package com.owl.noteowl.features.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        return NoteViewHolder(p0)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(p0: NoteViewHolder, p1: Int) {
    }

    inner class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    }
}