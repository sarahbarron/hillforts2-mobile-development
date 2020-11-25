package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_list.toolbar
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import org.wit.hillfort.R

class HillfortMapsActivity : AppCompatActivity() {
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        toolbar.title = title
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState);
    }
}