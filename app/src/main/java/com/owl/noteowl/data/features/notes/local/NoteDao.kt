package com.owl.noteowl.data.features.notes.local

import androidx.lifecycle.LiveData
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.asLiveData
import com.owl.noteowl.utils.Constants
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults

class NoteDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun defaultQuery(noteId: Int?): RealmQuery<Note>? {
        if (noteId != null) {
            return realm.where(Note::class.java)
                .equalTo("id", noteId)
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
            .findAll()
    }

    fun getSavedNotesLive(): LiveData<RealmResults<Note>> {
        return getSavedNotes().asLiveData()
    }

    fun saveStatus(noteId: Int, status: String) {
        realm.executeTransaction {
            defaultQuery(noteId)?.findFirst()?.let { note ->
                note.status = status
            }
        }
    }

    fun deleteNote(noteId: Int?) {
        defaultQuery(noteId)?.findFirst()?.deleteFromRealm()
    }

    fun close() {
        realm.close()
    }
}