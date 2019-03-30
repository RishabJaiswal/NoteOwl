package com.owl.noteowl.utils

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmObject

class RealmListLiveData<T : RealmObject>(private val realmList: RealmList<T>) : LiveData<RealmList<T>>() {

    private val realmChangeListener = RealmChangeListener<RealmList<T>> {
        value = it
    }

    override fun onActive() {
        super.onActive()
        realmList.addChangeListener(realmChangeListener)
        value = realmList
    }

    override fun onInactive() {
        super.onInactive()
        realmList.removeChangeListener(realmChangeListener)
    }
}