package com.owl.noteowl.features.noteImage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owl.noteowl.databinding.ItemSelectImageBinding
import com.owl.noteowl.utils.ContextUtility

class SelectImageAdapter(val context: Context) : RecyclerView.Adapter<SelectImageAdapter.ImageHolder>() {

    private val contextUtils by lazy { ContextUtility(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(
            ItemSelectImageBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        //todo:: change later
        return 10
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind()
    }

    inner class ImageHolder(val binding: ItemSelectImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }
}