package com.owl.noteowl.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.text(format: String): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    try {
        return simpleDateFormat.format(this)
    } catch (e: Exception) {
        return ""
    }
}