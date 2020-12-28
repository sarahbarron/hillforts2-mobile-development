package org.wit.hillfort.views.usersettings

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class UserSettingsPresenter(view:BaseView):BasePresenter(view) {


    init{

    }

    fun totalHillforts(): Int
    {
        var hillforts = app.hillforts.findAll()
        return hillforts.size
    }

    fun totalViewedHillforts(): Int{
        var hillforts = app.hillforts.findAll()
        var numVisited = 0
        for (hillfort in hillforts)
        {
            if (hillfort.visited) numVisited ++
        }
        return numVisited
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            FirebaseAuth.getInstance().currentUser!!.delete()
            uiThread {
                view?.finish()
            }
        }
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }


}