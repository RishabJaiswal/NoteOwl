package com.owl.noteowl.features.addNote

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import com.owl.noteowl.R
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.text
import com.owl.noteowl.extensions.visible
import kotlinx.android.synthetic.main.add_note.*
import java.util.*

class AddNoteActivity : Activity(), View.OnFocusChangeListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note)
        setLabels()
        tv_note_date.text = Date().text("dd MMM yyy")
        edt_note_text.setOnFocusChangeListener(this)
        cb_fav_note.setOnCheckedChangeListener(this)
    }

    //labels
    fun setLabels() {
        rv_labels_add_note.adapter =
                com.owl.noteowl.features.addNote.AddLabelAdapter(this, emptyList(), this::showSelectLabelDialog)
    }

    //focus listener
    override fun onFocusChange(view: View?, isFocused: Boolean) {
        //hiding all views above note text
        if (isFocused) {
            group_above_text.gone()
        } else {
            group_above_text.visible()
        }
    }

    //click listener
    override fun onClick(view: View?) {
        when (view?.id) {
        }
    }

    //checkbox change
    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            confetti_fav.speed = 1.3f
            confetti_fav.playAnimation()
        }
    }

    fun showSelectLabelDialog() {
        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_select_label)
            .create()
        dialog.show()
    }
}