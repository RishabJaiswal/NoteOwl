package com.owl.noteowl.data.features.notes.local

import com.owl.noteowl.data.features.notes.models.Label
import io.realm.Realm

class LabelDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun saveLabel(label: Label) {
        realm.executeTransaction {
            it.copyFromRealm(label)
        }
    }
}