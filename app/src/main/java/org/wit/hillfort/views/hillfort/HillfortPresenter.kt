package org.wit.hillfort.views.hillfort

import android.content.Intent
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.base.BaseView
import org.wit.hillfort.views.base.VIEW


class HillfortPresenter(view: BaseView) : BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val DELETE_IMAGE = 3

    var hillfort = HillfortModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;

    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(name: String, description: String) {
        hillfort.name = name
        hillfort.description = description
        if (edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.hillforts.delete(hillfort)
        view?.finish()
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        if(edit==false)
        {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
        }else{
            view?.navigateTo(
                VIEW.LOCATION,
                LOCATION_REQUEST,
                "location",
                Location(hillfort.lat, hillfort.lng, hillfort.zoom)
            )
        }
    }

    fun doSetVisited(checked: Boolean){
        if (checked) {
            app.hillforts.visited(hillfort, true)

        } else {
            app.hillforts.visited(hillfort, false)
        }
    }


    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                hillfort.images[0] = data.data.toString()
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.lat = location.lat
                hillfort.lng = location.lng
                hillfort.zoom = location.zoom
            }
        }
    }
}