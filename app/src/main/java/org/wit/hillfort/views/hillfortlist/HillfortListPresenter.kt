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
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.base.BaseView
import org.wit.hillfort.views.base.VIEW
import org.wit.hillfort.views.hillfort.HillfortView
import java.text.SimpleDateFormat
import java.util.*

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun getHillforts(userId: Long) = app.hillforts.findAll(userId)

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
       view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadHillforts(){
        view?.showHillforts(app.hillforts.findAll(0))
    }

    fun deleteAllHillforts(userId: Long){
        app.hillforts.deleteUserHillforts(userId)

    }
    fun doLogout(){
        view?.startActivity<AuthenticationActivity>()
    }

    fun doShowSettings(user: UserModel){
        view?.startActivity<UserSettingsActivity>()
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