package com.owl.noteowl.data.features.notes.local

import androidx.lifecycle.LiveData
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.extensions.asLiveData
import io.realm.Realm
import io.realm.RealmResults

class LabelDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun saveLabel(label: Label) {
        realm.executeTransaction {
            it.copyFromRealm(label)
        }
    }

    fun getLabels(): RealmResults<Label> {
        return realm.where(Label::class.java)
            .findAll()
    }

    fun getLabelsLive(): LiveData<RealmResults<Label>> {
        return getLabels().asLiveData()
    }
}