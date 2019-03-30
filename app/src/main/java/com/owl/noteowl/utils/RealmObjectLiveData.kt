package com.owl.noteowl.utils

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmObject

class RealmObjectLiveData<T : RealmObject>(private val realmObject: T) : LiveData<T>() {

    val realmChangeListener = RealmChangeListener<T> { newValue ->
        value = newValue
    }

    override fun onActive() {
        super.onActive()
        realmObject.addChangeListener(realmChangeListener)
        value = realmObject
    }

    override fun onInactive() {
        super.onInactive()
        realmObject.removeChangeListener(realmChangeListener)
    }
}