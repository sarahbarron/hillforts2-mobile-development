package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R

class HillfortMapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)

        // setup the toobar
        setSupportActionBar(toolbar)
        toolbar.title = title
    }
}