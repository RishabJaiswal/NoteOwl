package com.owl.noteowl.utils

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

class RealmResultsLiveData<T : RealmObject>(private val realmResults: RealmResults<T>) : LiveData<RealmResults<T>>() {
    private val realmResultsChangeListener = RealmChangeListener<RealmResults<T>> { updates ->
        value = updates
    }

    override fun onActive() {
        super.onActive()
        realmResults.addChangeListener(realmResultsChangeListener)
        value = realmResults
    }

    override fun onInactive() {
        super.onInactive()
        realmResults.removeChangeListener(realmResultsChangeListener)
    }
}