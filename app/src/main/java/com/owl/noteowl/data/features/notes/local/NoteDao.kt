package com.owl.noteowl.data.features.notes.local

import androidx.lifecycle.LiveData
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.data.features.notes.models.NoteFields
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.utils.Constants
import io.realm.*

class NoteDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun defaultQuery(noteId: Int?): RealmQuery<Note>? {
        if (noteId != null) {
            return realm.where(Note::class.java)
                .equalTo(NoteFields.ID, noteId)
        }
        return realm.where(Note::class.java)
    }

    fun saveNote(note: Note) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(note)
        }
    }

    fun saveLabels(noteId: Int?, labels: List<Label>) {
        defaultQuery(noteId)?.findFirst()?.let { note ->
            realm.executeTransaction {
                note.labels.addAll(labels)
            }
        }
    }

    fun getNote(noteId: Int?): Note? {
        return defaultQuery(noteId)?.findFirst()
    }

    fun getNoteLive(noteId: Int?): LiveData<Note>? {
        return getNote(noteId)?.asLiveData()
    }

    //saving image
    fun saveImage(noteId: Int, url: String) {
        if (url.isNotEmpty()) {
            defaultQuery(noteId)?.findFirst()?.let { note ->
                realm.executeTransaction {
                    note.imageUrl = url
                }
            }
        }
    }

    fun getSavedNotes(): RealmResults<Note> {
        return realm.where(Note::class.java)
            .equalTo("status", Constants.NoteStatus().SAVED)
            .sort(NoteFields.CREATED_AT, Sort.DESCENDING)
            .findAll()
    }

    fun getSavedNotesLive(): LiveData<RealmResults<Note>> {
        return getSavedNotes().asLiveData()
    }

    //get note labels live
    fun getNoteLabelsLive(noteId: Int?): LiveData<RealmList<Label>>? {
        return defaultQuery(noteId)
            ?.findFirst()?.labels
            ?.asLiveData()
    }

    //saving status
    fun saveStatus(noteId: Int?, status: String) {
        realm.executeTransaction {
            defaultQuery(noteId)?.findFirst()?.let { note ->
                note.status = status
            }
        }
    }

    //deleting note
    fun deleteNote(noteId: Int?) {
        defaultQuery(noteId)?.findFirst()?.let { note ->
            realm.executeTransaction {
                note.deleteFromRealm()
            }
        }
    }

    fun close() {
        realm.close()
    }
}