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

    fun loadHillforts(){
        doAsync {

            var hillforts: List<HillfortModel>
            if (view?.intent!!.hasExtra("hillfort_favourite")) {
                hillforts = app.hillforts.findFavourites()
            }
            else hillforts = app.hillforts.findAll()

            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun loadSearchedHillforts(text: String?){
        doAsync {
            var hillforts: List<HillfortModel>
            var fav:Boolean = view?.intent!!.hasExtra("hillfort_favourite")
            hillforts = app.hillforts.search(text, fav)
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
           val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
           val currentDateAndTime: String = simpleDateFormat.format(Date())
           hillfort.date = currentDateAndTime
           app.hillforts.visited(hillfort, true, currentDateAndTime)
           app.hillforts
       }
        else{
           app.hillforts.visited(hillfort, false, "")
           app.hillforts

       }
    }

    fun doSetFavourite(hillfort: HillfortModel, favourite: Boolean){
       app.hillforts.setFavourite(hillfort, favourite)
    }

    fun doViewFavourites(){ view?.navigateTo(VIEW.LIST, 0, "hillfort_favourite") }
    fun doViewHillfortsMap(){view?.navigateTo(VIEW.MAPS)}
    fun doViewHillforts(){view?.navigateTo(VIEW.LIST)}
    fun doViewSettings(){view?.navigateTo(VIEW.SETTINGS)}
}