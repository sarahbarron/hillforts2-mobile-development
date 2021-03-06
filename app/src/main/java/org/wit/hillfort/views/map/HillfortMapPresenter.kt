package org.wit.hillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW


class HillfortMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, hillforts: List<HillfortModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        hillforts.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }
    fun doMarkerSelected(marker: Marker) {
        val hillfort = marker.tag as HillfortModel
        doAsync {
            uiThread {
                if (hillfort != null) view?.showHillfort(hillfort)
            }
        }

    }

    fun loadHillforts(){
        doAsync {
            val hillforts = app.hillforts.findAll()
            uiThread{
                view?.showHillforts(hillforts)
            }
        }
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doViewFavourites(){ view?.navigateTo(VIEW.LIST, 0, "hillfort_favourite") }
    fun doViewHillforts(){view?.navigateTo(VIEW.LIST)}
    fun doViewSettings(){view?.navigateTo(VIEW.SETTINGS)}

}