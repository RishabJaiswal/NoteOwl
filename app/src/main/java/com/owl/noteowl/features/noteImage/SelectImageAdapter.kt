package com.owl.noteowl.features.noteImage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.R
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.databinding.ItemSelectImageBinding
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.openUrl
import com.owl.noteowl.extensions.parseHtml
import com.owl.noteowl.extensions.visible

class SelectImageAdapter(val context: Context,
                         val onImageSelected: (image: Image?) -> Unit) : RecyclerView.Adapter<SelectImageAdapter.ImageHolder>() {

    private val images = arrayListOf<Image>()
    private var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(
            ItemSelectImageBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateImages(images: List<Image>) {
        val diff = DiffUtil.calculateDiff(ImageDiffUtil(images))
        this.images.clear()
        this.images.addAll(images)
        diff.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(images[position])
    }

    //showing selected image
    private fun updateSelection(newPosition: Int) {
        val previousSelected = selectedItemPosition
        selectedItemPosition = newPosition
        if (previousSelected >= 0) {
            notifyItemChanged(previousSelected)
        }
        notifyItemChanged(selectedItemPosition)
    }

    fun getSelectedImage(): Image? {
        if (selectedItemPosition >= 0 && selectedItemPosition < images.size) {
            return images[selectedItemPosition]
        }
        return null
    }

    //image diff util
    inner class ImageDiffUtil(private val newImages: List<Image>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return images.size
        }

        override fun getNewListSize(): Int {
            return newImages.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return images[oldItemPosition].getDisplayImageUrl() == newImages[newItemPosition].getDisplayImageUrl()
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return images[oldItemPosition].getDisplayImageUrl() == newImages[newItemPosition].getDisplayImageUrl()
        }
    }

    inner class ImageHolder(val binding: ItemSelectImageBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.imvNoteImage.setOnClickListener(this)
            binding.imvNoteImage.clipToOutline = true
            binding.tvCredits.setOnClickListener(this)
        }

        fun bind(image: Image) {
            binding.image = image
            //showing selector
            if (selectedItemPosition == adapterPosition) {
                binding.selector.visible()
            } else {
                binding.selector.gone()
            }
            //photographer name
            binding.tvCredits.text = context.getString(
                R.string.image_credits,
                image.photographer?.name ?: ""
            ).parseHtml()
            binding.executePendingBindings()
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.imv_note_image -> {
                    updateSelection(adapterPosition)
                    onImageSelected(getSelectedImage())
                }
                R.id.tv_credits -> {
                    binding.image?.photographer?.links?.profilePagUrl?.let { profileUrl ->
                        context.openUrl(profileUrl)
                    }
                }
            }
        }
    }
}