package com.arimdor.sharednotes.app

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    companion object {
        val realm: Realm
            get() = Realm.getDefaultInstance()
        var photoUri = ""
    }

    override fun onCreate() {
        super.onCreate()
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)*/
        setUpRealmConfig()
    }

    private fun setUpRealmConfig() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}