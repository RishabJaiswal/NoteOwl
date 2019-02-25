package com.owl.noteowl.utils

import android.content.Context
import androidx.core.graphics.ColorUtils

class ContextUtility(val context: Context) {
    fun getTextColor(color: Int) = ColorUtils.calculateLuminance(color)
}