package org.wit.hillfort.views.hillfortlist

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.activities.AuthenticationActivity
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.usersettings.UserSettingsActivity
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfort.HillfortView
import java.text.SimpleDateFormat
import java.util.*

class HillfortListPresenter(val view: HillfortListView) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun getHillforts(userId: Long) = app.hillforts.findAll(userId)

    fun doAddHillfort() {
        view.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view.startActivityForResult(view.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort), 0)
    }

    fun doShowHillfortsMap() {
        view.startActivity<HillfortMapView>()
    }

    fun deleteAllHillforts(userId: Long){
        app.hillforts.deleteUserHillforts(userId)

    }
    fun doLogout(){
        view.startActivity<AuthenticationActivity>()
    }

    fun doShowSettings(user: UserModel){
        view.startActivity<UserSettingsActivity>()
    }

    fun doSetVisted(hillfort:HillfortModel, visited: Boolean){
       if (visited) {
           app.hillforts.visited(hillfort, true)
           val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
           val currentDateAndTime: String = simpleDateFormat.format(Date())
           hillfort.date = currentDateAndTime
       }
        else{
           app.hillforts.visited(hillfort, false)
           hillfort.date = ""
       }
    }
}