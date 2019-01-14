package com.owl.noteowl.data.features.notes.local

import com.owl.noteowl.data.features.notes.models.Note
import io.realm.Realm
import io.realm.RealmQuery

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

    fun getNote(noteId: Int): Note? {
        return defaultQuery(noteId)?.findFirst()
    }

    fun close() {
        realm.close()
    }
}