package com.owl.noteowl.features.noteImage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.databinding.ActivityAddNoteImageBinding
import com.owl.noteowl.extensions.text
import com.owl.noteowl.utils.Constants.Note
import kotlinx.android.synthetic.main.dialog_select_image.*

class AddNoteImageActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var viewModel: AddNoteImageViewModel
    val NOTE = Note()
    lateinit var binding: ActivityAddNoteImageBinding
    private var selectImageDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note_image)
        val noteId = intent.getIntExtra(NOTE.KEY_ID, 0)

        //creating view model
        viewModel = ViewModelProviders.of(
            this, AddNoteImageViewModel.Factory(noteId)
        )[AddNoteImageViewModel::class.java]

        binding.apply {
            note = viewModel.note
            tvNoteDate.text = viewModel.note?.createdAt?.text("dd MMM yyyy")
            icAddImage.setOnClickListener(this@AddNoteImageActivity)
            tvAddImage.setOnClickListener(this@AddNoteImageActivity)
            addImage.setOnClickListener(this@AddNoteImageActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //add image
            R.id.ic_add_image,
            R.id.tv_add_image,
            R.id.add_image -> {
                showSelectImageDialog()
            }
        }
    }

    //showing dialog
    fun showSelectImageDialog() {
        if (selectImageDialog == null) {
            selectImageDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_select_image)
                .create()
            rv_images.adapter = SelectImageAdapter(this)
        }
        selectImageDialog?.show()
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, AddNoteImageActivity::class.java).apply {
                putExtra(Note().KEY_ID, noteId)
            }
        }
    }
}
