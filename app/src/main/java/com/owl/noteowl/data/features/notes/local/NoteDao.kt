package com.owl.noteowl.data.features.notes.local

import com.owl.noteowl.data.features.notes.models.Note
import io.realm.Realm

class NoteDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun saveNote(note: Note) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(note)
        }
    }

    fun close() {
        realm.close()
    }
}