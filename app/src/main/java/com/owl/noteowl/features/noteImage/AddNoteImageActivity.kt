package com.owl.noteowl.features.noteImage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.databinding.ActivityAddNoteImageBinding
import com.owl.noteowl.extensions.text
import com.owl.noteowl.utils.Constants.Note

class AddNoteImageActivity : AppCompatActivity() {
    lateinit var viewModel: AddNoteImageViewModel
    val NOTE = Note()
    lateinit var binding: ActivityAddNoteImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note_image)
        val noteId = intent.getIntExtra(NOTE.KEY_ID, 0)

        //creating view model
        viewModel = ViewModelProviders.of(
            this, AddNoteImageViewModel.Factory(noteId)
        )[AddNoteImageViewModel::class.java]

        binding.note = viewModel.note
        binding.tvNoteDate.text = viewModel.note?.createdAt?.text("dd MMM yyyy")
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, AddNoteImageActivity::class.java).apply {
                putExtra(Note().KEY_ID, noteId)
            }
        }
    }
}
