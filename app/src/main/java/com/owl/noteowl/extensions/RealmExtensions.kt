package com.owl.noteowl.extensions

import androidx.lifecycle.LiveData
import com.owl.noteowl.utils.RealmObjectLiveData
import com.owl.noteowl.utils.RealmResultsLiveData
import io.realm.RealmObject
import io.realm.RealmResults

fun <T : RealmObject> T.asLiveData(): LiveData<T> {
    return RealmObjectLiveData(this)
}

fun <T : RealmObject> RealmResults<T>.asLiveData(): LiveData<RealmResults<T>> {
    return RealmResultsLiveData<T>(this)
}