package com.owl.noteowl

import android.app.Application
import io.realm.Realm

class NoteOwlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}