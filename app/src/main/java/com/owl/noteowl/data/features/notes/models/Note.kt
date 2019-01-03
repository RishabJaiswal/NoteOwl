package com.owl.noteowl.data.features.notes.models

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class Note : RealmObject() {
    var createdAt: Date = Date()
    var title: String = ""
    var text: String = ""
    var labels: RealmList<Label>? = null
    var isLiked: Boolean = false
}