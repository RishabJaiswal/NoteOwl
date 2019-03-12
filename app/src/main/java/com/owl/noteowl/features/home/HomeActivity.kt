package com.owl.noteowl.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.owl.noteowl.R
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.invisible
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.BaseActivity
import com.owl.noteowl.features.addNote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[NotesViewModel::class.java]
    }
    private var notesAdapter: NotesAdapter? = null
    private lateinit var labelsForFilterAdapter: LabelsForFilterAdapter
    val x by lazy { card_labels.left }
    val y by lazy { (card_labels.bottom - card_labels.top) / 2 }
    val endRadius by lazy {
        Math.hypot(card_labels.measuredWidth.toDouble(), card_labels.measuredWidth.toDouble()).toFloat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        observeNotes()
        observeLabels()
        btn_add_note.setOnClickListener(this)
        btn_filter_labels.setOnClickListener(this)
        btn_clear_filters.setOnClickListener(this)
        btn_add_note_home.setOnClickListener(this)
        btn_close_labels.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        hideFilters()
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
                labelsForFilterAdapter.clearFilter()
            }

            //add note
            R.id.btn_add_note_home -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
            }

            //filter labels
            R.id.btn_filter_labels -> {
                showFilters()
            }

            //close labels
            R.id.btn_close_labels -> {
                hideFilters()
            }
        }
    }

    private fun showFilters() {
        if (!card_labels.isVisible) {
            val anim = ViewAnimationUtils.createCircularReveal(card_labels, x, y, 0f, endRadius)
            anim.duration = 400L
            card_labels.visible()
            anim.start()
        }
    }

    private fun hideFilters() {
        if (card_labels.isVisible) {
            val anim = ViewAnimationUtils.createCircularReveal(card_labels, x, y, endRadius, 0f)
            anim.duration = 400L
            card_labels.invisible()
            anim.start()
        }
    }

    //observing notes list
    private fun observeNotes() {
        viewModel.notesLiveData.observe(this, Observer {
            it?.let { notes ->
                if (notes.isEmpty()) {
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
            ItemTouchHelper(LabelItemTouchListener()).attachToRecyclerView(rv_label_filter)
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
        val deleteNoteDialog: AlertDialog = AlertDialog.Builder(this, R.style.baseDialog)
            .setTitle(R.string.delete_note)
            .setPositiveButton(R.string.del_note_positive, null)
            .setNegativeButton(R.string.yes) { _, _ ->
                viewModel.deleteNote(note.id)
            }
            .create()
        deleteNoteDialog.setMessage(getString(R.string.delete_note_msg, note.title))
        deleteNoteDialog.show()
    }
}
