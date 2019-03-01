package com.owl.noteowl.features.addNote

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owl.noteowl.data.features.notes.local.LabelDao
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asNonManagedRealmCopy
import com.owl.noteowl.utils.Constants

class AddNoteViewModel(var noteId: Int?) : ViewModel() {

    val noteLabelsLiveData by lazy {
        MediatorLiveData<ArrayList<Label>>().apply {
            value = noteDao.getNote(noteId)?.labels?.asNonManagedRealmCopy() as ArrayList<Label>
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
        noteDao.getNoteLive(noteId)?.let { noteLive ->
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

    /**saving label
     * returns true when label is saved
     * returns false when label is already present
     * returns null when title or color of label is null*/
    fun saveLabel(position: Int? = null, labelTitle: String?, color: Int?): Boolean? {
        if (!labelTitle.isNullOrEmpty() && color != null) {
            noteLabelsLiveData.apply {
                //checking if label is already present
                if (!isLabelPresentInNote(labelTitle!!)) {

                    //creating label
                    val label = Label().apply {
                        title = labelTitle.trim()
                        colorHex = color
                    }

                    //saving label
                    if (position != null) {
                        value?.add(position, label)
                    } else {
                        value?.add(1, label)
                    }

                    //streaming changes
                    value = value
                    return true
                }
                return false
            }
        }
        return null
    }

    //checks if label is present in current note
    fun isLabelPresentInNote(labelTitle: String): Boolean {
        return noteLabelsLiveData.value?.find {
            it.title == labelTitle.trim()
        } != null
    }

    //checks if label is present in any note
    fun isLabelPresentInLabelsList(labelTitle: String): Boolean {
        return allLabelsLiveData.value?.find {
            it.title == labelTitle.trim()
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
    fun changeNoteStatus() {
        noteLiveData?.value?.let { note ->
            if (note.status == NOTE_STATUS.NEW_EDIT) {
                //new note
                noteDao.deleteNote(noteId)
            } else {
                //user was editing previous saved note
                noteDao.saveStatus(noteId, NOTE_STATUS.SAVED)
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