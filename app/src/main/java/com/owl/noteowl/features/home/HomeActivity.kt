package com.owl.noteowl.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.owl.noteowl.R
import com.owl.noteowl.analytics.ADD_NOTE
import com.owl.noteowl.analytics.Analytics
import com.owl.noteowl.analytics.DELETE_NOTE
import com.owl.noteowl.analytics.VIEW_NOTE
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.data.features.notes.models.Note
import com.owl.noteowl.extensions.*
import com.owl.noteowl.features.BaseActivity
import com.owl.noteowl.features.addNote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_options.*
import java.util.*


class HomeActivity : BaseActivity(), View.OnClickListener, SearchView.OnQueryTextListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[NotesViewModel::class.java]
    }
    private var notesAdapter: NotesAdapter? = null
    private lateinit var labelsForFilterAdapter: LabelsForFilterAdapter
    private val x by lazy { card_labels.left }
    private val y by lazy { (card_labels.bottom - card_labels.top) / 2 }
    private val endRadius by lazy {
        Math.hypot(card_labels.measuredWidth.toDouble(), card_labels.measuredWidth.toDouble()).toFloat()
    }
    private val blankSlateMsgs by lazy {
        resources.getStringArray(R.array.msgs_blank_slate_home)
    }

    private val optionsBottomSheet by lazy {
        BottomSheetBehavior.from(home_options).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_SETTLING) {
                        scrim_home.gone()
                    } else if (newState == BottomSheetBehavior.STATE_EXPANDED ||
                        newState == BottomSheetBehavior.STATE_HALF_EXPANDED
                    ) {
                        scrim_home.visible()
                    }
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        observeNotes()
        observeLabels()
        btn_quote.setOnClickListener(this)
        btn_filter_labels.setOnClickListener(this)
        btn_clear_filters.setOnClickListener(this)
        btn_add_note_home.setOnClickListener(this)
        btn_close_labels.setOnClickListener(this)
        sv_notes.setOnQueryTextListener(this)
        scrim_home.setOnClickListener(this)

        //options
        home_options.clipToOutline = true
        btn_options.setOnClickListener(this)
        option_rate_app.setOnClickListener(this)
        option_gift_dev.setOnClickListener(this)
        option_dev_apps.setOnClickListener(this)

        if (getCountryCode().toLowerCase() == "in") {
            option_gift_dev.visible()
        }
    }

    override fun onPause() {
        super.onPause()
        hideFilters()
        if (optionsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            optionsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onBackPressed() {
        if (optionsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            optionsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            //add note
            R.id.btn_add_note_home,
            R.id.btn_quote -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
                Analytics.track(ADD_NOTE)
            }

            //clear filters
            R.id.btn_clear_filters -> {
                clearFilters()
            }

            //filter labels
            R.id.btn_filter_labels -> {
                showFilters()
            }

            //close labels
            R.id.btn_close_labels -> {
                hideFilters()
            }

            //options
            R.id.btn_options -> {
                optionsBottomSheet.toggleState()
            }

            //rate app
            R.id.option_rate_app -> {
                openUrl(getString(R.string.link_app_play_store))
            }

            //gift dev
            R.id.option_gift_dev -> {
                openUrl(getString(R.string.link_dev_paytm))
            }

            //more apps
            R.id.option_dev_apps -> {
                openUrl(getString(R.string.link_dev_play_store))
            }

            //scrim
            R.id.scrim_home -> {
                optionsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun getCountryCode(): String {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.getSimCountryIso()
    }

    private fun clearFilters() {
        viewModel.clearFilters()
        labelsForFilterAdapter.clearFilter()
    }

    private fun showFilters() {
        if (!card_labels.isVisible) {
            btn_filter_labels.invisible()
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
            btn_filter_labels.visible()
        }
    }

    //observing notes list
    private fun observeNotes() {
        viewModel.notesLiveData.observe(this, Observer {
            it?.let { notes ->
                if (notes.isEmpty()) {
                    if (viewModel.isLabelsFilterEmpty()) {
                        //no label filter is selected
                        blank_slate_filters.gone()
                        if (viewModel.isSearchEmpty()) {
                            showBlankSlate()
                        }
                    } else {
                        hideBlankSlate()
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
            btn_quote.text = blankSlateMsgs[Random().nextInt(blankSlateMsgs.size)]
            blank_slate_home.visible()
        }
    }

    private fun hideBlankSlate() {
        blank_slate_home.gone()
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
            labelsForFilterAdapter = LabelsForFilterAdapter(this, viewModel)
            rv_label_filter.adapter = labelsForFilterAdapter
            ItemTouchHelper(LabelItemTouchListener(this)).attachToRecyclerView(rv_label_filter)
        }
        labelsForFilterAdapter.update(labels)
    }

    //on clicking note
    private fun onNoteClicked(note: Note) {
        startActivity(AddNoteActivity.getIntent(this, note.id))
        Analytics.track(VIEW_NOTE)
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

    /* search query callbacks starts*/
    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        clearFilters()
        viewModel.searchNotes(newText)
        return true
    }
    /* search query callbacks starts*/

    private fun showDeleteNoteDialog(note: Note) {
        val deleteNoteDialog: AlertDialog = AlertDialog.Builder(this, R.style.baseDialog)
            .setTitle(R.string.delete_note)
            .setPositiveButton(R.string.del_note_positive, null)
            .setNegativeButton(R.string.yes) { _, _ ->
                viewModel.deleteNote(note.id)
                Analytics.track(DELETE_NOTE)
            }
            .create()
        deleteNoteDialog.setMessage(getString(R.string.delete_note_msg, note.title))
        deleteNoteDialog.show()
    }
}
