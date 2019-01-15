package com.owl.noteowl.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.owl.noteowl.R
import com.squareup.picasso.Picasso

class ImageLoader {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:loadImage")
        fun loadImage(imv: ImageView, imageUrl: String) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_cross)
                .into(imv)
        }
    }
}
