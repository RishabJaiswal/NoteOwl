package com.owl.noteowl.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:bgTint")
fun setBgTint(view: View, color: String) {
    view.background?.let {
        DrawableCompat.wrap(it).apply {
            setTint(Color.parseColor(color))
            DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
        }
    }
}

@BindingAdapter("bind:bgTint")
fun setBgTint(view: View, color: Int) {
    view.background?.let {
        DrawableCompat.wrap(it).apply {
            setTint(color)
            DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
        }
    }
}