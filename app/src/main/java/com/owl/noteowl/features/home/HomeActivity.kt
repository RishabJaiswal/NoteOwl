package com.owl.noteowl.features.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.owl.noteowl.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_notes.adapter = NotesAdapter(this)
    }
}
