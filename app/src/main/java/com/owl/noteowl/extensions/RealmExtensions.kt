package com.owl.noteowl.extensions

import androidx.lifecycle.LiveData
import com.owl.noteowl.utils.RealmObjectLiveData
import io.realm.RealmObject

fun <T : RealmObject> T.asLiveData(): LiveData<T> {
    return RealmObjectLiveData(this)
}