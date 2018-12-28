package com.owl.noteowl.data.features.notes.models

import java.util.*

class Note {
    var title: String = ""
    var text: String = ""
    var createdAt: Date = Date()
    var labels: List<Label> = emptyList()
    var isLiked: Boolean = false
}