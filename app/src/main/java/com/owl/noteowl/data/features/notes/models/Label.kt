package com.owl.noteowl.data.features.notes.models

import android.graphics.Color
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import java.util.*

open class Label : RealmObject() {
    @PrimaryKey
    var title: String = ""
    var createdAt: Date = Date()
    var colorHex: Int = Color.RED

    @LinkingObjects("labels")
    val notes: RealmResults<Note>? = null
}