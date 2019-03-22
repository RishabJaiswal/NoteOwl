package com.owl.noteowl.features.home

import androidx.lifecycle.*
import com.owl.noteowl.data.features.notes.local.LabelDao
import com.owl.noteowl.data.features.notes.local.NoteDao
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asNonManagedRealmCopy

class NotesViewModel : ViewModel() {

    private val notesDao by lazy { NoteDao() }
    private val labelsDao by lazy { LabelDao() }

    //search filter
    private val searchQuery by lazy {
        MediatorLiveData<String>()
    }
    //contains ids of labels in filter in a list
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


        return@lazy MediatorLiveData<List<Note>>().apply {
            addSource(managedRealmListLive) {
                value = it.asNonManagedRealmCopy()
            }

            addSource(searchQuery) { query ->
                value = notesDao.searchNotes(query.trim())
            }
        }
    }

    val labelsLiveData = Transformations.map(labelsDao.getLabelsLive()) {
        it.asNonManagedRealmCopy()
    }

    //editing filter
    fun editFilter(labelId: String?) {
        if (labelId != null) {
            if (!labelsFilter.contains(labelId)) {
                labelsFilter.add(labelId)
            } else {
                labelsFilter.remove(labelId)
            }
            labelsFilterLive.value = labelsFilter
        }
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

    fun clearFilters() {
        labelsFilter.clear()
        labelsFilterLive.value = labelsFilter
    }

    fun getLabel(labelId: String): Label? {
        return labelsDao.getLabel(id = labelId)?.asNonManagedRealmCopy()
    }

    fun saveLabel(label: Label) {
        labelsDao.saveLabel(label)
    }

    fun searchNotes(query: String) {
        searchQuery.value = query
    }
}