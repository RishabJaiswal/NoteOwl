package com.owl.noteowl.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.addNote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[NotesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        observeNotes()
        observeLabels()
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

    //observing notes list
    private fun observeNotes() {
        viewModel.notesLiveData.observe(this, Observer {
            it?.let { notes ->
                if (notes.size == 0) {
                    blank_slate_home.visible()
                } else {
                    blank_slate_home.gone()
                    setNotes(notes)
                }
            }
        })
    }

    //observing labels list
    private fun observeLabels() {
        viewModel.labelsLiveData.observe(this, Observer {
            it?.let { labels ->
                setLabels(labels)
            }
        })
    }

    //setting notes list
    private fun setNotes(notes: List<Note>) {
        rv_notes.adapter = NotesAdapter(this, notes, this::onNoteClicked)
    }

    //setting labels
    private fun setLabels(labels: List<Label>) {
        rv_label_filter.adapter = LabelsForFilterAdapter(this, labels, this::onLabelClicked)
    }

    //on click label
    private fun onLabelClicked(label: Label?) {
        label?.let {
            //todo:: filter notes via label
        } ?: startActivity(Intent(this, AddNoteActivity::class.java))
    }

    //on clicking note
    private fun onNoteClicked(note: Note) {
        startActivity(AddNoteActivity.getIntent(this, note.id))
    }
}
