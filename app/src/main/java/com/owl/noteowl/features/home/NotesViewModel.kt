package com.owl.noteowl.features.home

import androidx.lifecycle.ViewModel
import com.owl.noteowl.data.features.notes.local.NoteDao

class NotesViewModel : ViewModel() {

    private val notesDao by lazy { NoteDao() }
    val notesLiveData = notesDao.getSavedNotesLive()
}