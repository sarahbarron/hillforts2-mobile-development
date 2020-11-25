package org.wit.hillfort.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.wit.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortName
import org.jetbrains.anko.*
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HillfortActivity : AppCompatActivity(), AnkoLogger, ImageListener {

    var hillfort = HillfortModel()
    var user = UserModel()
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val DELETE_IMAGE = 3
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        info("Hillforts activity started...")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title=title
        setSupportActionBar(toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        app = application as MainApp


        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager

        if(intent.hasExtra("user"))
        {
            user = intent.extras?.getParcelable<UserModel>("user")!!
            info("$user inside HillfortActivity")
        }

        if (intent.hasExtra("hillfort_edit")) {
            //        Create a Linear layout manager & tell the recyclerView to use this layout manager
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!

            dateVisited.setText("Date Visited: "+hillfort.date)
            headingHillfortName.setText(hillfort.name)
            hillfortName.setText(hillfort.name)
            hillfortDescription.setText(hillfort.description)
            hillfortNotes.setText(hillfort.notes)
            hillfortLat.setText("Lat: "+hillfort.lat)
            hillfortLng.setText("Lng: "+hillfort.lng)
            btnAdd.setText(R.string.save_hillfort)
            loadHillfort()

            if (hillfort.visited){
                visitedHillfort.isChecked = true
            }
        }

        chooseImage.setOnClickListener {
            info ("Select image")
            if(hillfort.images.size<4) {
                showImagePicker(this, IMAGE_REQUEST)
            }
            else
            {
                toast("Maximum number of images already saved")
            }
        }

        btnAdd.setOnClickListener() {
            hillfort.user = user.id
            hillfort.name = hillfortName.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.notes = hillfortNotes.text.toString()

            if (hillfort.name.isNotEmpty()) {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                    info("Edit: $hillfort")
                } else {
                    if (visitedHillfort.isChecked){
                        hillfort.visited = true
                    }
                    app.hillforts.create(hillfort.copy())
                    info("Create: $hillfort")
                }
            }
            else{
                toast(R.string.enter_hillfort_name)
            }
            info("Add Button Pressed: $hillfortName")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        hillfortLocation.setOnClickListener {
//            If the location placemark object's zoom is 0.0 use a default location
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.zoom != 0f) {
                location.lat =  hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location).putExtra("user", user), LOCATION_REQUEST)
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.visitedHillfort -> {
                    if (checked) {
                        app.hillforts.visited(hillfort, true)
                        val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
                        val currentDateAndTime: String = simpleDateFormat.format(Date())
                        hillfort.date = currentDateAndTime
                        dateVisited.setText("Date Visited: $currentDateAndTime")
                    } else {
                        app.hillforts.visited(hillfort, false)
                        hillfort.date = ""
                        dateVisited.setText("Date Visited: ")
                    }
                }
            }
        }
    }

//    retrieve the current hillfort from the JSON file
    fun loadHillfort(){
        hillfort = app.hillforts.findOne(hillfort.copy())
        showImages(hillfort.images)
    }

//    Show the current images
    fun showImages (images: ArrayList<String>) {
//        recycler View
        recyclerViewImages.adapter = ImageAdapter(images, this)
        recyclerViewImages.adapter?.notifyDataSetChanged()
//        set the text on the add images button
        if (hillfort.images.size > 0 && hillfort.images != null) {
            if(hillfort.images.size<4)
            {

                imageMessage.setText("Click on images to view or delete")
                chooseImage.setText(R.string.add_four_hillfort_image)
            }
            else{
                chooseImage.setText(R.string.max_hillfort_images)
                imageMessage.setText(" ")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.hillforts.delete(hillfort.copy())
                finish()
            }
            R.id.item_logout -> {
                startActivityForResult(intentFor<AuthenticationActivity>(),0)
            }
            R.id.item_settings ->  startActivityForResult(intentFor<UserSettingsActivity>().putExtra(
                "user",
                user
            ), 0)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                user = intent.extras?.getParcelable<UserModel>("user")!!
                if (data != null) {
                    hillfort.images.add(data.getData().toString())
                    if(hillfort.images.size < 4) {
                        chooseImage.setText(R.string.add_four_hillfort_image)
                    }
                    else{
                        chooseImage.setText(R.string.max_hillfort_images)
                    }
                    loadHillfort()
                }
            }
            LOCATION_REQUEST -> {
                user = intent.extras?.getParcelable<UserModel>("user")!!
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    hillfortLat.setText("Lat: "+hillfort.lat)
                    hillfortLng.setText("Lng: "+hillfort.lng)
                }
            }
            DELETE_IMAGE->{
                user = intent.extras?.getParcelable<UserModel>("user")!!
                loadHillfort()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onImageClick(image: String){
        startActivityForResult(intentFor<ImageActivity>().putExtra("image", image).putExtra("hillfort", hillfort).putExtra("user",user),DELETE_IMAGE)
    }

//   Functions needed to return the user to the HillfortListActivity after the Up navigation is pressed
    override fun onPrepareSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onPrepareSupportNavigateUpTaskStack(builder)
        builder.editIntentAt(builder.intentCount - 1)?.putExtra("user", user)
    }
    override fun supportShouldUpRecreateTask(targetIntent: Intent): Boolean {
        info("Hillfort: supportShouldUpRecreateTask")
        return true
    }
}