package com.owl.noteowl.features.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.owl.noteowl.databinding.DialogEditLabelBinding
import com.rtugeek.android.colorseekbar.ColorSeekBar

class EditLabelDialog(
    mContext: Context,
    val labelId: String,
    val viewModel: NotesViewModel
) : AlertDialog.Builder(mContext), View.OnClickListener, ColorSeekBar.OnColorChangeListener {

    lateinit var dialog: AlertDialog
    val binding = DialogEditLabelBinding.inflate(LayoutInflater.from(context)).apply {
        label = viewModel.getLabel(labelId)
        btnSaveLabel.setOnClickListener(this@EditLabelDialog)
        colorPicker.setOnColorChangeListener(this@EditLabelDialog)
    }

    override fun show(): AlertDialog {
        setView(binding.root)
        return super.show().apply {
            dialog = this
        }
    }

    override fun onClick(view: View?) {
        binding.label?.let {
            viewModel.saveLabel(it)
            dialog.dismiss()
        }
    }

    override fun onColorChangeListener(colorBarPosition: Int, alphaBarPosition: Int, color: Int) {
        binding.label?.colorHex = color
        binding.colorSelected = color
    }
}