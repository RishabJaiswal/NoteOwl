package com.owl.noteowl.data.features.notes.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note : RealmObject() {
    @PrimaryKey
    var id: Int = this.hashCode()
    var createdAt: Date = Date()
    var title: String = ""
    var text: String = ""
    var labels: RealmList<Label>? = null
    var isLiked: Boolean = false
}