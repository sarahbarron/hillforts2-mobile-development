package org.wit.hillfort.views.hillfortlist


import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import java.text.SimpleDateFormat
import java.util.*

class HillfortListPresenter(view: BaseView): BasePresenter(view) {

    fun getHillforts() = app.hillforts.findAll()


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

    fun deleteAllHillforts(){
        app.hillforts.deleteUserHillforts()

    }
    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doShowSettings(){
        view?.navigateTo(VIEW.SETTINGS)
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