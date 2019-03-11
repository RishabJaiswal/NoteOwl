package com.owl.noteowl.extensions

import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isViewTouched(ev: MotionEvent?): Boolean {
    val touchX = ev?.rawX ?: 0f
    val touchY = ev?.rawY ?: 0f
    return this.isVisible &&
            !(touchX > this.left && touchX < this.right && touchY > this.top && touchY < this.bottom)
}