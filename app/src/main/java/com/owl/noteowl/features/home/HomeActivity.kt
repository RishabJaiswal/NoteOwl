package com.owl.noteowl.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.owl.noteowl.R
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.visible
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //showing blankslate
        //todo: change later
        val notesSize = 1
        if (notesSize == 0) {
            blank_slate_home.visible()
        } else {
            blank_slate_home.gone()
            rv_notes.adapter = NotesAdapter(this)
        }
    }
}
