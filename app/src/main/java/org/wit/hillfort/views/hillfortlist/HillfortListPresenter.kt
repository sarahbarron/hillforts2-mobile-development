package org.wit.hillfort.views.hillfortlist

import androidx.core.content.ContextCompat.startActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import org.wit.hillfort.views.authenetication.AuthenticationActivity
import org.wit.hillfort.views.usersettings.UserSettingsActivity
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.map.HillfortMapView
import java.text.SimpleDateFormat
import java.util.*

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun getHillforts(userId: Long) = app.hillforts.findAll()


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
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun deleteAllHillforts(userId: Long){
        app.hillforts.deleteUserHillforts(userId)

    }
    fun doLogout(){
        view?.auth?.signOut()
        view?.startActivity<AuthenticationActivity>()
        view?.finish()
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