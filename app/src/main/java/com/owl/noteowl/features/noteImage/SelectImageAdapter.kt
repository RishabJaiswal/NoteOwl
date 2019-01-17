package com.owl.noteowl.features.noteImage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.databinding.ItemSelectImageBinding
import com.owl.noteowl.utils.ContextUtility

class SelectImageAdapter(val context: Context) : RecyclerView.Adapter<SelectImageAdapter.ImageHolder>() {

    private val contextUtils by lazy { ContextUtility(context) }
    private val images = arrayListOf<Image>()

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

    fun addImages(images: List<Image>) {
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(images[position])
    }

    inner class ImageHolder(val binding: ItemSelectImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {
            binding.imageUrl = image.urls?.regular
            binding.executePendingBindings()
        }
    }
}