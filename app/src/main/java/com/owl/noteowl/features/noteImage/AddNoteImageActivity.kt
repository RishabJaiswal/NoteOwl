package com.owl.noteowl.features.noteImage

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.databinding.ActivityAddNoteImageBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.text
import com.owl.noteowl.utils.Constants.Note
import com.owl.noteowl.utils.ContextUtility
import kotlinx.android.synthetic.main.dialog_note_saved.view.*
import kotlinx.android.synthetic.main.dialog_select_image.*
import kotlinx.android.synthetic.main.dialog_select_image.view.*

class AddNoteImageActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var viewModel: AddNoteImageViewModel
    val NOTE = Note()
    lateinit var binding: ActivityAddNoteImageBinding
    private var selectImageDialog: AlertDialog? = null
    private lateinit var imagesAdapter: SelectImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note_image)
        val noteId = intent.getIntExtra(NOTE.KEY_ID, 0)

        //creating view model
        viewModel = ViewModelProviders.of(
            this, AddNoteImageViewModel.Factory(noteId)
        )[AddNoteImageViewModel::class.java]
        observeNote()

        binding.apply {
            icAddImage.setOnClickListener(this@AddNoteImageActivity)
            tvAddImage.setOnClickListener(this@AddNoteImageActivity)
            addImage.setOnClickListener(this@AddNoteImageActivity)
            btnSave.setOnClickListener(this@AddNoteImageActivity)
            addImage.clipToOutline = true
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

            //select an image
            R.id.btn_select -> {
                imagesAdapter.getSelectedImage()?.getDisplayImageUrl()?.let { imageUrl ->
                    viewModel.saveImage(imageUrl)
                    selectImageDialog?.dismiss()
                }
            }

            //saving image
            R.id.btn_save -> {
                viewModel.saveNote()
                showNoteSavedDialog()
                Handler().postDelayed({
                    finish()
                }, 1500)
            }
        }
    }

    //showing dialog
    fun showSelectImageDialog() {
        if (selectImageDialog == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_select_image, null)
            selectImageDialog = AlertDialog.Builder(this)
                .setView(view)
                .create()

            //images recycler listing
            val margin = ContextUtility(this).convertDpToPx(10f).toInt()
            imagesAdapter = SelectImageAdapter(this)
            view.rv_images.adapter = imagesAdapter
            view.rv_images.addItemDecoration(ImagesDecoration(margin, margin))
            view.btn_select.setOnClickListener(this)
            viewModel.getImages()
            observeImages()
        }
        selectImageDialog?.show()
    }

    //observing note changes
    private fun observeNote() {
        viewModel.noteLiveData?.observe(this, Observer {
            it?.let { note ->
                binding.note = note
                binding.tvNoteDate.text = note.createdAt.text("dd MMM yyyy")
            }
        })
    }

    //observing images
    private fun observeImages() {
        viewModel.imagesLiveData.observe(this, Observer<List<Image>> { images ->
            selectImageDialog?.pb_images?.gone()
            imagesAdapter.addImages(images)
        })
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, AddNoteImageActivity::class.java).apply {
                putExtra(Note().KEY_ID, noteId)
            }
        }
    }

    //for images grid list
    class ImagesDecoration(val rightMargin: Int, val bottomMargin: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildLayoutPosition(view)
            if (position == 0 || position % 2 == 0) {
                outRect.right = rightMargin
            }
            outRect.bottom = bottomMargin
        }
    }

    //showing note saved
    fun showNoteSavedDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_note_saved, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
        view.lottie_note_saved.playAnimation()
        alertDialog.show()
    }
}
