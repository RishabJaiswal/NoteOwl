package com.owl.noteowl.features.addNote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.notes.local.LabelDao
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.extensions.asNonManagedRealmCopy
import com.owl.noteowl.utils.Constants
import io.realm.RealmList

class AddNoteViewModel(var noteId: Int?) : ViewModel() {

    val noteLabelsLiveData by lazy {
        MutableLiveData<RealmList<Label>>().apply {
            value = noteLiveData?.value?.labels ?: RealmList()
        }
    }

    val allLabelsLiveData by lazy {
        labelDao.getLabelsLive()
    }

    val NOTE_STATUS = Constants.NoteStatus()
    val noteLiveData by lazy {
        if (noteId == null) {
            val note = Note().apply {
                this.status = NOTE_STATUS.NEW_EDIT
                noteId = this.id
            }
            noteDao.saveNote(note)
        } else {
            //changing status of an already saved note
            noteDao.saveStatus(noteId, NOTE_STATUS.SAVED_EDIT)
        }
        noteDao.getNote(noteId)?.asLiveData()?.let { noteLive ->
            Transformations.map(noteLive) { note ->
                note.asNonManagedRealmCopy()
            }
        }
    }

    private val noteDao by lazy { NoteDao() }
    private val labelDao by lazy { LabelDao() }
    private val addLabel by lazy { noteLabelsLiveData.value?.first() }

    //adding "add Label" to the labels list of note
    fun setAddLabel(name: String?, color: Int?) {
        saveLabel(0, name, color)
    }

    //saving label
    fun saveLabel(position: Int? = null, name: String?, color: Int?) {
        if (!name.isNullOrEmpty() && color != null) {
            noteLabelsLiveData.apply {
                //creating label
                val label = Label().apply {
                    title = name!!
                    colorHex = color
                }

                //checking if label is already present
                val isLabelPresent = isLabelPresent(label, value ?: emptyList())
                if (isLabelPresent == false) {
                    //saving label
                    if (position != null) {
                        value?.add(position, label)
                    } else {
                        value?.add(1, label)
                    }

                    //streaming changes
                    value = value
                }
            }
        }
    }

    fun isLabelPresent(label: Label, labels: List<Label>): Boolean? {
        return labels.find {
            it.title == label.title
        } != null
    }

    //removing label
    fun removeLabel(position: Int) {
        noteLabelsLiveData.apply {
            value?.removeAt(position)
            value = value
        }
    }

    //saving note
    fun saveNote() {
        noteLiveData?.value?.apply {
            noteLabelsLiveData.value?.let { labels ->
                this.labels.clear()
                this.labels.addAll(labels.filter {
                    it.title != addLabel?.title
                })
            }
            noteDao.saveNote(this)
        }
    }

    //deleting note
    fun deleteNote() {
        noteLiveData?.value?.let { note ->
            if (note.status == NOTE_STATUS.NEW_EDIT) {
                noteDao.deleteNote(noteId)
            }
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