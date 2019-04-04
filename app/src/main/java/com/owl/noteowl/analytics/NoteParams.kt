package com.owl.noteowl.analytics

import com.owl.noteowl.data.features.notes.models.Note
import java.io.Serializable

const val ADD_NOTE = "add_note"
const val VIEW_NOTE = "view_note"
const val LIKE_NOTE = "note_like"
const val UNLIKE_NOTE = "note_unlike"
const val ADD_NOTE_IMAGE = "add_note_image"
const val CHANGE_NOTE_IMAGE = "change_note_image"
const val SAVE_NOTE = "save_note"
const val DELETE_NOTE = "delete_note"

const val PARAM_TITLE_SIZE = "title_size"
const val PARAM_TEXT_SIZE = "text_size"
const val PARAM_LABELS_COUNT = "labels_count"
const val PARAM_HAS_IMAGE = "contains_image"
const val PARAM_LIKED = "is_liked"

fun trackNote(note: Note): Map<String, Serializable> {
    return HashMap<String, Serializable>().apply {
        put(PARAM_TITLE_SIZE, note.title.length)
        put(PARAM_TEXT_SIZE, note.text.length)
        put(PARAM_LABELS_COUNT, note.labels.size)
        put(PARAM_HAS_IMAGE, note.imageUrl.isNotEmpty())
        put(PARAM_LIKED, note.isLiked)
    }
}