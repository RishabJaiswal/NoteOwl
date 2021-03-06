package com.owl.noteowl.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class ImageLoader {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:loadImage")
        fun loadImage(imv: ImageView, imageUrl: String) {
            if (!imageUrl.isEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .into(imv)
            } else {
                imv.setImageDrawable(null)
            }
        }
    }
}
