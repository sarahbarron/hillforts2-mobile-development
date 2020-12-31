package org.wit.hillfort.views.usersettings

import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class UserSettingsPresenter(view:BaseView):BasePresenter(view) {
    var auth: FirebaseAuth
    var user: FirebaseUser

    init{
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
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

            app.hillforts.deleteUserHillforts()
            app.hillforts.clear()
            user.delete()
            view?.navigateTo(VIEW.LOGIN)
        }
    }

    fun doLogout() {
        auth.signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun updateSettings(email: String, password: String)
    {
        user.updateEmail(email)
        if (password.length>0) {
            user.updatePassword(password)
        }
    }

    fun doShowHillfortList(){
        view?.navigateTo(VIEW.LIST)
    }

    fun doShowHillfortsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun doShowFavourites(){

    }
}