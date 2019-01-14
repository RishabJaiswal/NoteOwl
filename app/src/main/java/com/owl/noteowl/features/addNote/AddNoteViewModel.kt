package com.owl.noteowl.features.addNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.utils.Constants.NoteStatus
import io.realm.RealmList

class AddNoteViewModel : ViewModel() {
    var labelsLiveData = MutableLiveData<RealmList<Label>>().apply {
        value = RealmList()
    }

    val newNote by lazy {
        Note().apply {
            this.status = NoteStatus().NEW_EDIT
        }
    }

    val noteDao by lazy { NoteDao() }

    //saving label
    fun saveLabel(name: String?, color: Int?) {
        if (!name.isNullOrEmpty() && color != null) {
            labelsLiveData.apply {
                value?.add(Label().apply {
                    title = name!!
                    colorHex = color
                })
                value = value
            }
        }
    }

    //removing label
    fun removeLabel(position: Int) {
        labelsLiveData.apply {
            value?.removeAt(position)
            value = value
        }
    }

    //saving note
    fun saveNote() {
        noteDao.saveNote(newNote)
    }
}