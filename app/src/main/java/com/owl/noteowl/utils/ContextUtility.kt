package com.owl.noteowl.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.owl.noteowl.R

class ContextUtility(val context: Context) {

    val resources by lazy { context.resources }
    val POSITION by lazy { Constants.Position() }
    val inputManager by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    //getting bgColor for a given background bgColor
    fun getTextColor(bgColor: Int): Int {
        if (ColorUtils.calculateLuminance(bgColor) < 0.5)
            return ContextCompat.getColor(context, android.R.color.white)
        return ContextCompat.getColor(context, R.color.textSecondary)
    }

    fun getMargin(viewType: Int, firstMargin: Int, midMargin: Int, endMargin: Int): Float {
        val margin = if (viewType == POSITION.FIRST) {
            firstMargin
        } else if (viewType == POSITION.MID) {
            midMargin
        } else {
            endMargin
        }
        return convertDpToPx(margin.toFloat())
    }

    fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    fun closeKeyboard(view: View) {
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}