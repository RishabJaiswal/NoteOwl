package com.owl.noteowl.data.features.notes.local

import androidx.lifecycle.LiveData
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.LabelFields
import com.owl.noteowl.extensions.asLiveData
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort

class LabelDao(val realm: Realm = Realm.getDefaultInstance()) {

    fun defaultQuery(labelId: String? = null, labelTitle: String? = null): RealmQuery<Label> {
        var query = realm.where(Label::class.java)
        if (!labelId.isNullOrEmpty()) {
            query = query.equalTo(LabelFields.ID, labelId)
        }
        if (!labelTitle.isNullOrEmpty()) {
            query = query.equalTo(LabelFields.TITLE, labelTitle)
        }
        return query
    }

    fun saveLabel(label: Label) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(label)
        }
    }

    fun getLabels(): RealmResults<Label> {
        return realm.where(Label::class.java)
                .sort(LabelFields.CREATED_AT, Sort.DESCENDING)
                .findAll()
    }

    fun getLabelsLive(): LiveData<RealmResults<Label>> {
        return getLabels().asLiveData()
    }

    fun getLabel(id: String? = null, title: String? = null): Label? {
        return defaultQuery(labelId = id, labelTitle = title).findFirst()
    }

    fun deleteLabel(id: String) {
        realm.executeTransaction {
            defaultQuery(labelId = id)
                    .findFirst()?.deleteFromRealm()
        }
    }
}