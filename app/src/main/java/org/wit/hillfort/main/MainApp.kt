package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*


// Main App - retrieve both hillforts and users from the JSON stores
class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        users=UserJSONStore(applicationContext)
        info("Hillfort started")
    }
}