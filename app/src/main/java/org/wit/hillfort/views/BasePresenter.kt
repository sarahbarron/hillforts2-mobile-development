package org.wit.hillfort.views

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.views.BaseView

open class BasePresenter(var view: BaseView?) {

    var app: MainApp =  view?.application as MainApp

    open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    }

    open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

    open fun onDestroy() {
        view = null
    }
}