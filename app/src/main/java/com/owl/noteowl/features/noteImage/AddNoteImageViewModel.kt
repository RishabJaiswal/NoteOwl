package com.owl.noteowl.features.noteImage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.notes.local.NoteDao

class AddNoteImageViewModel(val noteId: Int) : ViewModel() {
    private val noteDao = NoteDao()
    val note = noteDao.getNote(noteId)

    class Factory(val noteId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNoteImageViewModel::class.java)) {
                return AddNoteImageViewModel(noteId) as T
            } else {
                throw IllegalArgumentException("Not a valid view model")
            }
        }
    }
}