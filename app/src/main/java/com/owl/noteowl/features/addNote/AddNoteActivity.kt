package com.owl.noteowl.features.addNote

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.databinding.AddNoteBinding
import com.owl.noteowl.databinding.DialogSelectLabelBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.invisible
import com.owl.noteowl.extensions.text
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.features.noteImage.AddNoteImageActivity
import com.owl.noteowl.utils.Constants
import com.owl.noteowl.utils.ContextUtility
import io.realm.RealmList
import kotlinx.android.synthetic.main.add_note.*
import java.util.*

class AddNoteActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private var selectLabelDialog: AlertDialog? = null
    private var exitDialog: AlertDialog? = null
    private val contextUtils by lazy { ContextUtility(this) }
    private lateinit var selectLabelBinding: DialogSelectLabelBinding
    private lateinit var mainBinding: AddNoteBinding

    private val viewModel by lazy {
        ViewModelProviders.of(
            this, AddNoteViewModel.Factory(null)
        )[AddNoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.add_note)
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

        edt_note_text.setOnFocusChangeListener(this)
        btn_edit_title.setOnClickListener(this)
        cb_fav_note.setOnCheckedChangeListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onBackPressed() {
        showConfirmExitDialog()
    }

    //labels
    fun setLabels() {
        viewModel.setAddLabel(getString(R.string.add_label), Color.parseColor("#c6c6c6"))
        val addLabelAdapter = AddLabelAdapter(this, RealmList(), viewModel, this::showSelectLabelDialog)
        rv_labels_add_note.adapter = addLabelAdapter

        //observing added labels
        viewModel.labelsLiveData.observe(this, androidx.lifecycle.Observer {
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
            hideTitle()
        } else {
            editTitle()
        }
    }

    private fun observeNote() {
        viewModel.noteLiveData?.observe(this, Observer {
            it?.let { note ->
                if (note.status === Constants.NoteStatus().SAVED) {
                    finish()
                } else {
                    mainBinding.note = note
                }
            }
        })
    }

    //showing hiding title
    fun editTitle() {
        btn_edit_title.gone()
        edt_note_text.clearFocus()
        edt_note_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
        rv_labels_add_note.visible()
    }

    fun hideTitle() {
        btn_edit_title.visible()
        edt_note_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        rv_labels_add_note.gone()
    }

    //click listener
    override fun onClick(view: View?) {
        when (view?.id) {
            //cancel label select
            R.id.btn_cancel_select_label -> {
                selectLabelDialog?.dismiss()
            }

            //select label
            R.id.btn_save_select_label -> {
                if (selectLabelBinding.labelName.isNullOrEmpty()) {
                    Toast.makeText(this, getString(R.string.error_empty_label), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.saveLabel(null, selectLabelBinding.labelName, selectLabelBinding.colorSelected)
                }
                selectLabelDialog?.dismiss()
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
        }
    }

    //checkbox change
    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            confetti_fav.speed = 1.3f
            confetti_fav.playAnimation()
        }
    }

    //dialog helps in selecting or creating a label
    fun showSelectLabelDialog() {
        if (selectLabelDialog == null) {
            selectLabelBinding = DialogSelectLabelBinding.inflate(layoutInflater)
            selectLabelBinding.HSLColorPicker.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
                selectLabelBinding.colorSelected = color
            }
            //click listeners
            selectLabelBinding.btnCancelSelectLabel.setOnClickListener(this)
            selectLabelBinding.btnSaveSelectLabel.setOnClickListener(this)

            //creating dialog
            selectLabelDialog = AlertDialog.Builder(this)
                .setView(selectLabelBinding.root)
                .create()
        }

        //selecting random color and emptying label title
        selectLabelBinding.edtLabelTitle.setText("")
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
}