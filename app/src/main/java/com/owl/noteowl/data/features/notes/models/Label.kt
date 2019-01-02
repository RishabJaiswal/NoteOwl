package com.owl.noteowl.data.features.notes.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

class Label : RealmObject() {
    @PrimaryKey
    var title: String = ""
    var createdAt: Date = Date()
    var colorHex: String = "#000000"
}