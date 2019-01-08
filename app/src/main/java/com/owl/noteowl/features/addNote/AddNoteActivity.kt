package com.owl.noteowl.features.addNote

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.databinding.DialogSelectLabelBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.text
import com.owl.noteowl.extensions.visible
import io.realm.RealmList
import kotlinx.android.synthetic.main.add_note.*
import java.util.*

class AddNoteActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private var selectLabelDialog: AlertDialog? = null
    private lateinit var selectLabelBinding: DialogSelectLabelBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this)[AddNoteViewModel::class.java]
    }

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
        viewModel.saveLabel(getString(R.string.add_label), Color.parseColor("#c6c6c6"))
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
            group_above_text.gone()
        } else {
            group_above_text.visible()
        }
    }

    //click listener
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_cancel_select_label -> {
                selectLabelDialog?.dismiss()
            }

            R.id.btn_save_select_label -> {
                viewModel.saveLabel(selectLabelBinding.labelName, selectLabelBinding.colorSelected)
                selectLabelDialog?.dismiss()
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
        selectLabelDialog?.show()
    }
}