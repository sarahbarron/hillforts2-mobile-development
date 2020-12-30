package org.wit.hillfort.views.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.mapView
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.info
import org.wit.hillfort.R

import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class HillfortMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: HillfortMapPresenter
    lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        super.init(toolbar, true)

        presenter = initPresenter(HillfortMapPresenter(this)) as HillfortMapPresenter

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
           map = it
            map.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        if(hillfort.date == "")
        {
            currentVisited.text = "Not visited yet"
        }
        else {
            currentVisited.text = hillfort.date
        }
        buttonMapEditCurrentHillfort.visibility = View.VISIBLE
        currentName.text = hillfort.name
        currentDescription.text = hillfort.description

        Glide.with(this).load(hillfort.images[0]).into(currentImage);

        buttonMapEditCurrentHillfort.setOnClickListener(){
            presenter.doEditHillfort(hillfort)
        }
    }

    override fun showHillforts(hillforts: List<HillfortModel>){
        presenter.doPopulateMap(map, hillforts)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    // Refreshes the view when a hillfort is updated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        currentName.text = "Hillfort Name"
        currentDescription.text = "Hillfort Description"
        currentVisited.text = "Date Visited"
        buttonMapEditCurrentHillfort.visibility = View.INVISIBLE
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)

    }
}