package com.owl.noteowl.features

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        //hiding status bar
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    protected fun showToast(stringRes: Int) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
    }

    protected fun <T> start(clazz: Class<T>) {
        startActivity(Intent(this, clazz))
    }
}