package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.models.json.HillfortJSONStore
import org.wit.hillfort.models.room.HillfortStoreRoom


// Main App - retrieve both hillforts and users from the JSON stores
class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortFireStore(applicationContext)
        info("Hillfort started")
    }
}