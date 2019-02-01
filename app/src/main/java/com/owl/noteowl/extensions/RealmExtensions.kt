package com.owl.noteowl.extensions

import androidx.lifecycle.LiveData
import com.owl.noteowl.utils.RealmListLiveData
import com.owl.noteowl.utils.RealmObjectLiveData
import com.owl.noteowl.utils.RealmResultsLiveData
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.RealmResults

//Realm object live data
fun <T : RealmObject> T.asLiveData(): LiveData<T> {
    return RealmObjectLiveData(this)
}

//Realm results live data
fun <T : RealmObject> RealmResults<T>.asLiveData(): LiveData<RealmResults<T>> {
    return RealmResultsLiveData<T>(this)
}

//Realm list live data
fun <T : RealmObject> RealmList<T>.asLiveData(): LiveData<RealmList<T>> {
    return RealmListLiveData<T>(this)
}

fun <T : RealmObject> T.asNonManagedRealmCopy(realm: Realm = Realm.getDefaultInstance()): T {
    return realm.copyFromRealm(this)
}
