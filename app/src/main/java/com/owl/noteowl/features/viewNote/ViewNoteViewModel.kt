package com.owl.noteowl.features.viewNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.notes.local.NoteDao

class ViewNoteViewModel(private val noteId: Int) : ViewModel() {

    private val noteDao = NoteDao()
    val noteLiveData = noteDao.getNoteLive(noteId)

    class Factory(var noteId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ViewNoteViewModel::class.java)) {
                return ViewNoteViewModel(noteId) as T
            } else {
                throw IllegalArgumentException("Not a valid view model")
            }
        }
    }
}