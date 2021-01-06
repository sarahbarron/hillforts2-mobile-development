package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.*
import androidx.core.content.FileProvider


class HillfortPresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var hillfort = HillfortModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()
    var locationManualyChanged = false;
    var image_count = 0;

    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            view.btnDeleteHillfort.visibility = View.VISIBLE
            view.shareBtn.visibility = View.VISIBLE
            hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
                doSetEmptyImages()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetEmptyImages(){
        hillfort.images = arrayListOf<String>()
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    if(!locationManualyChanged) {
                        locationUpdate(Location(l.latitude, l.longitude))
                    }
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun cacheHillfort (name: String, description: String, notes: String, visited: Boolean, date: String, rating: Float, favourite: Boolean) {
        hillfort.name = name;
        hillfort.description = description
        hillfort.notes = notes
        hillfort.visited = visited
        hillfort.date = date
        hillfort.rating = rating
        hillfort.favourite = favourite
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.location)
    }

    fun locationUpdate(location: Location) {
        hillfort.location = location
        hillfort.location.zoom = 15f
        map?.clear()
        val options = MarkerOptions().title(hillfort.name).position(LatLng(hillfort.location.lat, hillfort.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
        view?.showLocation(hillfort.location)
    }

    fun doAddOrSave(name: String, description: String, notes: String, visited: Boolean, date: String, rating: Float, favourite: Boolean) {
        hillfort.name = name
        hillfort.description = description
        hillfort.notes = notes
        hillfort.visited = visited
        hillfort.date = date
        hillfort.rating = rating
        hillfort.favourite = favourite
        doAsync {
            if (edit) {
                app.hillforts.update(hillfort)
            } else {
                app.hillforts.create(hillfort)
            }
            image_count = 0
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        doAsync {
            app.hillforts.delete(hillfort)
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

    fun doSelectImage():Boolean{
        if(hillfort.images.size < 4) {
            view?.let {
                showImagePicker(view!!, IMAGE_REQUEST)
            }
            return true
        }
        return false
    }

    fun doSelectCameraImage():Boolean{
        if(hillfort.images.size < 4) {
            if (checkImagePersmission(view!!)) {
                view?.let {
                    showCameraPicker(view!!, CAMERA_REQUEST)
                }
            } else requestImagePermission(view!!)
            return true
        }
        return false
    }

    fun doSetLocation() {
        locationManualyChanged = true;
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
    }

    fun doSetVisited(visited: Boolean, date: String){
        hillfort.visited = visited;
        hillfort.date = date;
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                if(data!=null) {
                        hillfort.images.add(data.getData().toString())

                    view?.showHillfort(hillfort)
                }
            }
            CAMERA_REQUEST ->{
                if(data!=null){
                    hillfort.images.add(getCurrentPhotoPath()!!)
                }
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.location = location
                locationUpdate(location)
            }
        }
    }


    // return current photo path
    fun getCurrentPhotoPath(): String? {
        return mCurrentPhotoPath
    }

    fun getImages()= hillfort.images

    fun loadImages(){
        doAsync{
            val images = hillfort.images
            uiThread{
                view?.showImages(images)
            }
        }
    }

    fun doViewFavourites(){ view?.navigateTo(VIEW.LIST, 0, "hillfort_favourite") }
    fun doViewHillfortsMap(){view?.navigateTo(VIEW.MAPS)}
    fun doViewHillforts(){view?.navigateTo(VIEW.LIST)}
}