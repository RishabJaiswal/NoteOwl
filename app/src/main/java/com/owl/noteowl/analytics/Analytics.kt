package com.owl.noteowl.analytics

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.Serializable

object Analytics {
    private lateinit var appContext: Application
    private val analytics by lazy {
        FirebaseAnalytics.getInstance(appContext)
    }

    public fun init(appContext: Application) {
        this.appContext = appContext
    }

    //tracking event
    public fun track(eventName: String, eventParams: Map<String, Serializable>? = null) {
        if (eventParams == null) {
            analytics.logEvent(eventName, null)
        } else {
            analytics.logEvent(eventName, Bundle().apply {
                for (param in eventParams.entries) {
                    putSerializable(param.key, param.value)
                }
            })
        }
    }
}