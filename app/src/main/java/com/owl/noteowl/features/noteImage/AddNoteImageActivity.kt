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
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.Result
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.databinding.ActivityAddNoteImageBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.text
import com.owl.noteowl.extensions.visible
import com.owl.noteowl.utils.Constants.Note
import com.owl.noteowl.utils.ContextUtility
import kotlinx.android.synthetic.main.dialog_note_saved.view.*
import kotlinx.android.synthetic.main.dialog_select_image.*
import kotlinx.android.synthetic.main.dialog_select_image.view.*

class AddNoteImageActivity : AppCompatActivity(), View.OnClickListener, SearchView.OnQueryTextListener {
    lateinit var viewModel: AddNoteImageViewModel
    val NOTE = Note()
    lateinit var binding: ActivityAddNoteImageBinding
    private var selectImageDialog: AlertDialog? = null
    private lateinit var imagesAdapter: SelectImageAdapter
    private lateinit var imagesScrollListener: RecyclerView.OnScrollListener

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
    private fun showSelectImageDialog() {
        if (selectImageDialog == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_select_image, null)
            selectImageDialog = AlertDialog.Builder(this)
                .setView(view)
                .create()

            //images recycler listing
            val margin = ContextUtility(this).convertDpToPx(10f).toInt()
            imagesAdapter = SelectImageAdapter(this)
            view.apply {
                //search view
                sv_image.isIconified = false
                sv_image.clearFocus()
                sv_image.setOnQueryTextListener(this@AddNoteImageActivity)

                /*images list
                adding pagination for image listing*/
                val layoutManager = rv_images.layoutManager as LinearLayoutManager
                imagesScrollListener = object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val isScrolledToLast = firstVisibleItem + visibleItemCount >= totalItemCount
                        if (viewModel.canLoadMoreImages() && isScrolledToLast) {
                            if (viewModel.currentSearchQuery.isNullOrEmpty()) {
                                viewModel.getRandomImages()
                            } else {
                                viewModel.searchImages(viewModel.currentSearchQuery!!)
                            }
                        }
                    }
                }
                rv_images.adapter = imagesAdapter
                rv_images.addItemDecoration(ImagesDecoration(margin, margin))
                rv_images.addOnScrollListener(imagesScrollListener)
                btn_select.setOnClickListener(this@AddNoteImageActivity)
            }
            viewModel.getRandomImages()
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
        viewModel.imagesLiveData.observe(this, Observer<Result<List<Image>>> {
            it?.parseResponse(
                //progress
                { isLoading ->
                    if (isLoading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                },

                //success
                { images ->
                    imagesAdapter.updateImages(images)
                    hideProgress()
                },

                //error
                { errorThrowable ->
                    hideProgress()
                })
        })
    }

    private fun showProgress() {
        selectImageDialog?.apply {
            if (rv_images?.layoutManager?.itemCount == 0) {
                pb_images.visible()
            } else {
                pb_images_more?.visible()
            }
        }
    }

    private fun hideProgress() {
        selectImageDialog?.pb_images?.gone()
        selectImageDialog?.pb_images_more?.gone()
    }

    //search query callback starts
    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.clearImages()
            viewModel.searchImages(it)
        }
        return true
    }
    //search query callback ends

    //for images grid list
    class ImagesDecoration(private val rightMargin: Int, private val bottomMargin: Int) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildLayoutPosition(view)
            if (position == 0 || position % 2 == 0) {
                outRect.right = rightMargin / 2
            } else {
                outRect.left = rightMargin / 2
            }
            outRect.bottom = bottomMargin
        }
    }

    //showing note saved
    private fun showNoteSavedDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_note_saved, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
        view.lottie_note_saved.playAnimation()
        alertDialog.show()
    }

    companion object {
        fun getIntent(context: Context, noteId: Int): Intent {
            return Intent(context, AddNoteImageActivity::class.java).apply {
                putExtra(Note().KEY_ID, noteId)
            }
        }
    }
}
