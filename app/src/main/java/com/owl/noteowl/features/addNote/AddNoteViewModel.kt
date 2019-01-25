package com.owl.noteowl.features.addNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.utils.Constants.NoteStatus

class AddNoteViewModel : ViewModel() {
    var labelsLiveData = MutableLiveData<ArrayList<Label>>().apply {
        value = arrayListOf()
    }

    private val newNote by lazy {
        Note().apply {
            this.status = NoteStatus().NEW_EDIT
        }
    }

    val noteLiveData by lazy {
        MutableLiveData<Note>().apply {
            this.value = newNote
        }
    }

    private val noteDao by lazy { NoteDao() }
    private val addLabel by lazy {
        labelsLiveData.value?.first()
    }

    //adding "add Label" to the labels list of note
    fun setAddLabel(name: String?, color: Int?) {
        saveLabel(0, name, color)
    }

    //saving label
    fun saveLabel(position: Int? = null, name: String?, color: Int?) {
        if (!name.isNullOrEmpty() && color != null) {
            labelsLiveData.apply {
                //creating label
                val label = Label().apply {
                    title = name!!
                    colorHex = color
                }

                //saving label
                if (position != null) {
                    value?.add(position, label)
                } else {
                    value?.add(label)
                }

                //streaming changes
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
        noteDao.saveNote(newNote.apply {
            labelsLiveData.value?.let { labels ->
                this.labels.addAll(labels.filter {
                    it.title != addLabel?.title
                })
            }
        })
    }

    fun getNoteId() = newNote.id
}