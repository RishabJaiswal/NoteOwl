package com.owl.noteowl.realm

import io.realm.DynamicRealm
import io.realm.RealmMigration

class Migrations() : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
    }

    companion object {
        val DB_VERSION = 1L
    }
}