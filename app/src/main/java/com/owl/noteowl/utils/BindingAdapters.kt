package com.owl.noteowl.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.LinearLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:bgTint")
fun setBgTint(view: LinearLayout, color: String) {
    view.background?.let {
        DrawableCompat.wrap(it).apply {
            setTint(Color.parseColor(color))
            DrawableCompat.setTintMode(this, PorterDuff.Mode.MULTIPLY)
        }
    }
}