package com.owl.noteowl.features.addNote

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.analytics.Analytics
import com.owl.noteowl.analytics.LIKE_NOTE
import com.owl.noteowl.analytics.UNLIKE_NOTE
import com.owl.noteowl.data.features.notes.models.Label
import com.owl.noteowl.databinding.ActivityAddNoteBinding
import com.owl.noteowl.databinding.DialogCreateLabelBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.invisible
import com.owl.noteowl.extensions.text
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.BaseActivity
import com.owl.noteowl.features.noteImage.AddNoteImageActivity
import com.owl.noteowl.utils.Constants
import com.owl.noteowl.utils.ContextUtility
import com.owl.noteowl.utils.visibleOrGone
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*

class AddNoteActivity : BaseActivity(), View.OnFocusChangeListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private var selectLabelDialog: AlertDialog? = null
    private var exitDialog: AlertDialog? = null
    private val contextUtils by lazy { ContextUtility(this) }
    private lateinit var selectLabelBinding: DialogCreateLabelBinding
    private lateinit var mainBinding: ActivityAddNoteBinding

    private val viewModel by lazy {
        var noteId: Int? = intent.getIntExtra(Constants.Note().KEY_ID, -1)
        if (noteId == -1) {
            noteId = null
        }
        return@lazy ViewModelProviders.of(
            this, AddNoteViewModel.Factory(noteId)
        )[AddNoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_note)
        observeNote()
        setLabels()
        tv_note_date.text = Date().text("dd MMM yyy")

        //setting text change listener
        edt_note_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable?.trim()?.length == 0) {
                    btn_next.invisible()
                } else {
                    btn_next.visible()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        edt_note_text.onFocusChangeListener = this
        btn_edit_title.setOnClickListener(this)
        cb_fav_note.setOnCheckedChangeListener(this)
        cb_fav_note.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onBackPressed() {
        showConfirmExitDialog()
    }

    override fun onDestroy() {
        viewModel.changeNoteStatus()
        super.onDestroy()
    }

    //labels
    fun setLabels() {
        viewModel.setAddLabel(getString(R.string.add_label), Color.parseColor("#c6c6c6"))
        val addLabelAdapter = AddLabelAdapter(this, RealmList(), viewModel, this::showSelectLabelDialog)
        rv_labels_add_note.adapter = addLabelAdapter

        //observing added labels
        viewModel.noteLabelsLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let { labels ->
                //adding last added label
                if (labels.size > 0) {
                    addLabelAdapter.update(labels)
                }
            }
        })
    }

    //focus listener
    override fun onFocusChange(view: View?, isFocused: Boolean) {
        //hiding all views above note text
        if (isFocused) {
            editText()
            group_blank_slate.gone()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        } else {
            editTitle()
            //showing blank slate
            if (edt_note_text.length() == 0) {
                group_blank_slate.visible()
            }
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
    }

    private fun observeNote() {
        viewModel.noteLiveData?.observe(this, Observer {
            it?.let { note ->
                if (note.status == Constants.NoteStatus().SAVED) {
                    finish()
                } else {
                    mainBinding.note = note
                }
            }
        })
    }

    //showing hiding title
    private fun editTitle() {
        btn_edit_title.gone()
        edt_note_text.clearFocus()
        group_date_fav_labels.visible()
        rv_labels_add_note.visible()
        edt_note_title.maxLines = 3
        edt_note_title.setSelection(0)
    }

    private fun editText() {
        btn_edit_title.visible()
        rv_labels_add_note.gone()
        group_date_fav_labels.gone()
        edt_note_title.maxLines = 1
    }

    //click listener
    override fun onClick(view: View?) {
        when (view?.id) {
            //cancel label select
            R.id.btn_cancel_select_label -> {
                selectLabelDialog?.dismiss()
            }

            //select label
            R.id.btn_save_label -> {
                selectLabelBinding.label?.let { label ->
                    val labelTitle = label.title.trim()
                    if (labelTitle.isEmpty()) {
                        showToast(R.string.error_empty_label)
                    } else {
                        if (viewModel.isLabelPresentInLabelsList(labelTitle) ||
                            viewModel.saveLabel(null, label) == false
                        ) {
                            showToast(R.string.error_saving_label)
                        } else {
                            selectLabelDialog?.dismiss()
                        }
                    }
                }
            }

            //next
            R.id.btn_next -> {
                viewModel.saveNote()
                viewModel.noteId?.let { noteId ->
                    startActivity(AddNoteImageActivity.getIntent(this, noteId))
                }
            }

            //showing title
            R.id.btn_edit_title -> {
                editTitle()
                contextUtils.closeKeyboard(edt_note_text)
            }

            //fav note
            R.id.cb_fav_note -> {
                animateFavCheckbox()
                trackFav()
            }
        }
    }

    //checkbox change
    override fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        animateFavCheckbox()
    }

    //animating confetti
    private fun animateFavCheckbox() {
        if (cb_fav_note.isChecked) {
            confetti_fav.speed = 1.3f
            confetti_fav.playAnimation()
        }
    }

    private fun trackFav() {
        Analytics.track(if (cb_fav_note.isChecked) LIKE_NOTE else UNLIKE_NOTE)
    }

    //observing saved changes in database
    private fun observeSavedLabels() {
        viewModel.allLabelsLiveData.observe(this, Observer {
            it?.let { labels ->
                visibleOrGone(selectLabelBinding.tvLblSelectOne, labels.size > 0)
                selectLabelBinding.rvSelectLabel.adapter = LabelsSelectAdapter(this, labels, this::onLabelClicked)
            }
        })
    }

    private fun onLabelClicked(label: Label?) {
        label?.let {
            selectLabelDialog?.dismiss()
            viewModel.saveLabel(null, label)
        }
    }

    //dialog helps in selecting or creating a label
    fun showSelectLabelDialog() {
        if (selectLabelDialog == null) {
            selectLabelBinding = DialogCreateLabelBinding.inflate(layoutInflater)
            selectLabelBinding.apply {
                colorPicker.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
                    label?.colorHex = color
                    colorSelected = color
                }
                //click listeners
                btnSaveLabel.setOnClickListener(this@AddNoteActivity)

                //creating dialog
                selectLabelDialog = AlertDialog.Builder(this@AddNoteActivity)
                    .setView(root)
                    .create()
            }
            observeSavedLabels()
        }

        //selecting random color and emptying label title
        selectLabelBinding.label = Label()
        selectLabelBinding.edtLabelDescription.setText("")
        selectLabelBinding.edtLabelName.setText("")
        selectLabelDialog?.show()
    }

    //exit confirmation
    fun showConfirmExitDialog() {
        if (exitDialog == null) {
            exitDialog = AlertDialog.Builder(this, R.style.baseDialog)
                .setTitle(R.string.exit)
                .setMessage(R.string.exit_confirmation)
                .setPositiveButton(R.string.confirm_exit_msg, null)
                .setNegativeButton(R.string.yes) { _, _ ->
                    finish()
                }
                .create()
        }
        exitDialog?.show()
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, AddNoteActivity::class.java).apply {
                putExtra(Constants.Note().KEY_ID, noteId)
            }
        }
    }
}