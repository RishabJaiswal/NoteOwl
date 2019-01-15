package com.owl.noteowl.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.owl.noteowl.extensions.gone
import com.owl.noteowl.extensions.visible

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

@BindingAdapter("android:layout_marginStart")
fun setStartMargin(view: View, startMargin: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        Math.round(startMargin), layoutParams.topMargin,
        layoutParams.rightMargin, layoutParams.bottomMargin
    );
    view.setLayoutParams(layoutParams)
}

@BindingAdapter("android:layout_marginEnd")
fun setEndMargin(view: View, endMargin: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin, layoutParams.topMargin,
        Math.round(endMargin), layoutParams.bottomMargin
    );
    view.setLayoutParams(layoutParams)
}

//setting visibility gone or visible
@BindingAdapter("bind:visibleOrGone")
fun visibleOrGone(view: View, isConditionTrue: Boolean) {
    if (isConditionTrue)
        view.visible()
    else
        view.gone()
}