package com.owl.noteowl.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.BaseActivity
import com.owl.noteowl.features.addNote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[NotesViewModel::class.java]
    }
    private var deleteNoteDialog: AlertDialog? = null
    private var notesAdapter: NotesAdapter? = null
    private lateinit var labelsForFilterAdapter: LabelsForFilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        observeNotes()
        observeLabels()
        btn_add_note.setOnClickListener(this)
        btn_clear_filters.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            //add note
            R.id.btn_add_note -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
            }

            //clear filters
            R.id.btn_clear_filters -> {
                viewModel.clearFilters()
            }
        }
    }

    //observing notes list
    private fun observeNotes() {
        viewModel.notesLiveData.observe(this, Observer {
            it?.let { notes ->
                if (notes.size == 0) {
                    if (viewModel.isLabelsFilterEmpty()) {
                        //no label filter is selected
                        showBlankSlate()
                    } else {
                        blank_slate_filters.visible()
                    }
                } else {
                    blank_slate_filters.gone()
                    blank_slate_home.gone()
                }
                setNotes(notes)
            }
        })
    }

    private fun showBlankSlate() {
        if (viewModel.isLabelsFilterEmpty()) {
            blank_slate_home.visible()
        }
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
        if (notesAdapter == null) {
            notesAdapter = NotesAdapter(this, arrayListOf(), this::onNoteClicked, this::onNoteActionClicked)
            rv_notes.adapter = notesAdapter
        }
        notesAdapter?.update(notes)
    }


    //setting labels
    private fun setLabels(labels: List<Label>) {
        if (rv_label_filter.adapter == null) {
            labelsForFilterAdapter = LabelsForFilterAdapter(this, viewModel, this::onLabelClicked)
            rv_label_filter.adapter = labelsForFilterAdapter
        }
        labelsForFilterAdapter.update(labels)
    }

    //on click label
    private fun onLabelClicked(label: Label?) {
        label?.let {
            viewModel.editFilter(label.title)
        } ?: startActivity(Intent(this, AddNoteActivity::class.java))
    }

    //on clicking note
    private fun onNoteClicked(note: Note) {
        startActivity(AddNoteActivity.getIntent(this, note.id))
    }

    //on note actions clicked
    private fun onNoteActionClicked(menuItemId: Int?, note: Note) {
        when (menuItemId) {
            //edit note
            R.id.edit -> {
                startActivity(AddNoteActivity.getIntent(this, note.id))
            }

            //delete note
            R.id.delete -> {
                showDeleteNoteDialog(note)
            }
        }
    }

    private fun showDeleteNoteDialog(note: Note) {
        if (deleteNoteDialog == null) {
            deleteNoteDialog = AlertDialog.Builder(this, R.style.baseDialog)
                .setTitle(R.string.delete_note)
                .setPositiveButton(R.string.del_note_positive, null)
                .setNegativeButton(R.string.yes) { _, _ ->
                    viewModel.deleteNote(note.id)
                }
                .create()
        }
        deleteNoteDialog?.setMessage(getString(R.string.delete_note_msg, note.title))
        deleteNoteDialog?.show()
    }
}
