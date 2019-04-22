package com.owl.noteowl

import android.app.Application
import com.owl.noteowl.analytics.Analytics
import com.owl.noteowl.realm.Migrations
import io.realm.Realm
import io.realm.RealmConfiguration

class NoteOwlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupRealm()
        Analytics.init(this)
    }

    private fun setupRealm() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .name("noteOwl.realm")
            .schemaVersion(Migrations.DB_VERSION)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
        Realm.getInstance(realmConfig)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }
}