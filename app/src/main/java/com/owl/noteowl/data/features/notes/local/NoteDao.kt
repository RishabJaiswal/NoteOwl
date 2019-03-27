package com.owl.noteowl.data.features.notes.local

import androidx.lifecycle.LiveData
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.data.features.notes.models.NoteFields
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.utils.Constants
import io.realm.*

class NoteDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun defaultQuery(noteId: Int? = null): RealmQuery<Note> {
        val query = realm.where(Note::class.java)
        if (noteId != null) {
            return query.equalTo(NoteFields.ID, noteId)
        }
        return query
    }

    fun saveNote(note: Note) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(note)
        }
    }

    fun saveLabels(noteId: Int?, labels: List<Label>) {
        defaultQuery(noteId).findFirst()?.let { note ->
            realm.executeTransaction {
                note.labels.addAll(labels)
            }
        }
    }

    fun getNote(noteId: Int?): Note? {
        return defaultQuery(noteId).findFirst()
    }

    fun getNoteLive(noteId: Int?): LiveData<Note>? {
        return getNote(noteId)?.asLiveData()
    }

    //saving image
    fun saveImage(noteId: Int, url: String) {
        if (url.isNotEmpty()) {
            defaultQuery(noteId).findFirst()?.let { note ->
                realm.executeTransaction {
                    note.imageUrl = url
                }
            }
        }
    }

    fun getSavedNotes(): RealmResults<Note> {
        return realm.where(Note::class.java)
                .contains(NoteFields.STATUS, Constants.NoteStatus().SAVED)
                .sort(NoteFields.CREATED_AT, Sort.DESCENDING)
                .findAll()
    }

    fun getSavedNotesLive(): LiveData<RealmResults<Note>> {
        return getSavedNotes().asLiveData()
    }

    //get note labels live
    fun getNoteLabelsLive(noteId: Int?): LiveData<RealmList<Label>>? {
        return defaultQuery(noteId)
                .findFirst()?.labels
                ?.asLiveData()
    }

    //saving status
    fun saveStatus(noteId: Int?, status: String) {
        realm.executeTransaction {
            defaultQuery(noteId).findFirst()?.let { note ->
                note.status = status
            }
        }
    }

    //deleting note
    fun deleteNote(noteId: Int?) {
        realm.executeTransaction {
            defaultQuery(noteId).findFirst()?.deleteFromRealm()
        }
    }

    //getting labels by label ids
    fun getNotesByLabelAsync(labelIds: Array<String>): RealmResults<Note> {
        return defaultQuery()
                .`in`(NoteFields.LABELS.ID, labelIds)
                .sort(NoteFields.CREATED_AT, Sort.DESCENDING)
                .findAllAsync()
    }

    fun getNotesByLabelAsyncLive(labelIds: Array<String>): LiveData<RealmResults<Note>>? {
        return getNotesByLabelAsync(labelIds).asLiveData()
    }

    fun searchNotes(query: String): RealmResults<Note> {
        return defaultQuery()
                .beginGroup()
                .contains(NoteFields.TITLE, query, Case.INSENSITIVE)
                .or()
                .contains(NoteFields.TEXT, query, Case.INSENSITIVE)
                .endGroup()
                .sort(NoteFields.CREATED_AT, Sort.DESCENDING)
                .findAll()
    }

    fun close() {
        realm.close()
    }
}