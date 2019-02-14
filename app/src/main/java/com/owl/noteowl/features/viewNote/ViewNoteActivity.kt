package com.owl.noteowl.features.viewNote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.owl.noteowl.R
import com.owl.noteowl.databinding.ActivityViewNoteBinding
import com.owl.noteowl.utils.Constants

class ViewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding
    private val viewModel by lazy {
        val noteId: Int = intent.getIntExtra(Constants.Note().KEY_ID, -1)
        return@lazy ViewModelProviders.of(
            this, ViewNoteViewModel.Factory(noteId)
        )[ViewNoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_note)
        observeNote()
    }

    private fun observeNote() {
        viewModel.noteLiveData?.observe(this, Observer {
            binding.note = it
        })
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, ViewNoteActivity::class.java).apply {
                putExtra(Constants.Note().KEY_ID, noteId)
            }
        }
    }
}