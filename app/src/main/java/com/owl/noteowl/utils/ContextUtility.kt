package com.owl.noteowl.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.owl.noteowl.R

class ContextUtility(val context: Context) {

    //getting bgColor for a given background bgColor
    fun getTextColor(bgColor: Int): Int {
        if (ColorUtils.calculateLuminance(bgColor) < 0.5)
            return ContextCompat.getColor(context, android.R.color.white)
        return ContextCompat.getColor(context, R.color.textSecondary)
    }
}