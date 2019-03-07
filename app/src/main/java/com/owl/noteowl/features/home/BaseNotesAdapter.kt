package com.owl.noteowl.features.home

import android.content.Context
import com.owl.noteowl.data.features.notes.models.Note

interface BaseNotesAdapter {
    fun update(newNotes: List<Note>)
}