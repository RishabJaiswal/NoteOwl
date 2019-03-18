package com.owl.noteowl

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import com.owl.noteowl.features.BaseActivity
import com.owl.noteowl.features.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        (imv_logo.drawable as Animatable).start()

        Handler().postDelayed({
            start(HomeActivity::class.java)
            finish()
        }, 600)
    }
}
