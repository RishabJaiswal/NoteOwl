package com.owl.noteowl.features.addNote

import android.app.Activity
import android.os.Bundle
import com.owl.noteowl.R
import kotlinx.android.synthetic.main.add_note.*

class AddNoteActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note)
        setLabels()
    }

    fun setLabels() {
        rv_labels_add_note.adapter = AddLabelAdapter(this, emptyList())
    }
}