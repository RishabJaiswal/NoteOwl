package com.owl.noteowl.features.addNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.extensions.asNonManagedRealmCopy
import com.owl.noteowl.utils.Constants

class AddNoteViewModel(var noteId: Int?) : ViewModel() {

    var labelsLiveData = MutableLiveData<ArrayList<Label>>().apply {
        value = arrayListOf()
    }

    val noteLiveData by lazy {
        if (noteId == null) {
            val note = Note().apply {
                this.status = Constants.NoteStatus().NEW_EDIT
                noteId = this.id
            }
            noteDao.saveNote(note)
        }
        noteDao.getNote(noteId)?.asLiveData()?.let { noteLive ->
            Transformations.map(noteLive) { note ->
                note.asNonManagedRealmCopy()
            }
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
        noteLiveData?.value?.apply {
            labelsLiveData.value?.let { labels ->
                this.labels.addAll(labels.filter {
                    it.title != addLabel?.title
                })
            }
            noteDao.saveNote(this)
        }
    }

    class Factory(var noteId: Int?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
                return AddNoteViewModel(noteId) as T
            } else {
                throw IllegalArgumentException("Not a valid view model")
            }
        }
    }
}