package com.owl.noteowl.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.owl.noteowl.data.features.notes.local.LabelDao
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asNonManagedRealmCopy

class NotesViewModel : ViewModel() {

    private val notesDao by lazy { NoteDao() }
    private val labelsDao by lazy { LabelDao() }

    private val labelsFilter = arrayListOf<String>()
    private val labelsFilterLive by lazy {
        MutableLiveData<ArrayList<String>>().apply {
            value = labelsFilter
        }
    }

    //notes by filter
    val notesLiveData: LiveData<List<Note>> by lazy {
        val managedRealmListLive = Transformations.switchMap(labelsFilterLive) { labels ->
            if (labels == null || labels.isEmpty()) {
                return@switchMap notesDao.getSavedNotesLive()
            }
            return@switchMap notesDao.getNotesByLabelAsyncLive(labels.toTypedArray())
        }
        return@lazy Transformations.map(managedRealmListLive) {
            it.asNonManagedRealmCopy()
        }
    }

    val labelsLiveData = labelsDao.getLabelsLive()

    //editing filter
    fun editFilter(labelTitle: String) {
        if (!labelsFilter.contains(labelTitle)) {
            labelsFilter.add(labelTitle)
        } else {
            labelsFilter.remove(labelTitle)
        }
        labelsFilterLive.value = labelsFilter
    }

    fun containsLabelInFilter(labelTitle: String): Boolean {
        return labelsFilter.contains(labelTitle)
    }

    //deleting note
    fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    fun isLabelsFilterEmpty(): Boolean {
        return labelsFilter.size == 0
    }
}