package com.owl.noteowl.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.owl.noteowl.R
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.addNote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //showing blankslate
        //todo: change later
        val notesSize = 0
        if (notesSize == 0) {
            blank_slate_home.visible()
        } else {
            blank_slate_home.gone()
            rv_notes.adapter = NotesAdapter(this)
        }
        btn_add_note.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            //add note
            R.id.btn_add_note -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
            }
        }
    }
}
