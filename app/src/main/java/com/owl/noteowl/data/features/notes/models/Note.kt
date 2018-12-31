package com.owl.noteowl.data.features.notes.models

import io.realm.RealmObject
import java.util.*

class Note : RealmObject() {
    var createdAt: Date = Date()
    var title: String = ""
    var text: String = ""
    var labels: List<Label> = emptyList()
    var isLiked: Boolean = false
}