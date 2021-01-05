package org.wit.hillfort.views.hillfort
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.RatingBar
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.wit.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortName
import kotlinx.android.synthetic.main.activity_hillfort.mapView
import kotlinx.android.synthetic.main.activity_hillfort_maps.bottom_navigation
import org.jetbrains.anko.*
import org.wit.hillfort.activities.ImageAdapter
import org.wit.hillfort.activities.ImageListener
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.FileProvider


class HillfortView : BaseView(), AnkoLogger, ImageListener {

    lateinit var presenter: HillfortPresenter
    var hillfort = HillfortModel()
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        init(toolbarAdd,true)

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

        presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter

        chooseImage.setOnClickListener{
                presenter.cacheHillfort(
                    hillfortName.text.toString(),
                    hillfortDescription.text.toString(),
                    hillfortNotes.text.toString(),
                    visitedHillfort.isChecked,
                    dateVisited.text.toString(),
                    hillfortRating.rating,
                    hillfortFavourite.isChecked
                )
                presenter.doSelectImage()
        }

        btn_camera.setOnClickListener {
            presenter.cacheHillfort(
                hillfortName.text.toString(),
                hillfortDescription.text.toString(),
                hillfortNotes.text.toString(),
                visitedHillfort.isChecked,
                dateVisited.text.toString(),
                hillfortRating.rating,
                hillfortFavourite.isChecked
            )
            presenter.doSelectCameraImage()
        }

        hillfortLocation.setOnClickListener{
            presenter.cacheHillfort(
                hillfortName.text.toString(),
                hillfortDescription.text.toString(),
                hillfortNotes.text.toString(),
                visitedHillfort.isChecked,
                dateVisited.text.toString(),
                hillfortRating.rating,
                hillfortFavourite.isChecked
            )
            presenter.doSetLocation()
        }

        hillfortRating.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                hillfortRating.rating = p1
            }
        })

        val layoutManager = LinearLayoutManager(this)
        recyclerViewImages.layoutManager = layoutManager
        recyclerViewImages.adapter = ImageAdapter(presenter.getImages(), this)
        recyclerViewImages.adapter?.notifyDataSetChanged()
        presenter.loadImages()


        btnAdd.setOnClickListener() {
            if (hillfortName.text.toString().isEmpty()) {
                toast(R.string.enter_hillfort_name)
            }
            else{
                presenter.doAddOrSave(
                    hillfortName.text.toString(),
                    hillfortDescription.text.toString(),
                    hillfortNotes.text.toString(),
                    visitedHillfort.isChecked,
                    dateVisited.text.toString(),
                    hillfortRating.rating,
                    hillfortFavourite.isChecked
                )
            }
        }
        btnDeleteHillfort.setOnClickListener(){
            presenter.doDelete()
        }

        shareBtn.setOnClickListener{
            btnDeleteHillfort.visibility = View.INVISIBLE
            shareBtn.visibility = View.INVISIBLE
            emailAddress.visibility = View.VISIBLE
            sendEmailBtn.visibility = View.VISIBLE
        }

        sendEmailBtn.setOnClickListener{
            val email = emailAddress.text.toString()
            val subject = "Hillfort: "+hillfortName.text.toString()
            val name = hillfortName.text.toString().trim()
            val description = hillfortDescription.text.toString().trim()
            val notes = hillfortDescription.text.toString().trim()
            val lat = hillfortLat.text.toString().trim()
            val lng = hillfortLng.text.toString().trim()
            val rating = hillfortRating.rating
            val message = "Hillfort Name: \t $name,\n Description: \t $description,\n Notes: \t $notes, \n Location:\t $lat lat, $lng lng, \n Rating: \t $rating"
            sendEmail(email, subject, message)
        }


        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomMain -> {presenter.doViewHillforts()
                    true}
                R.id.bottomFavourites -> {presenter.doViewFavourites()
                 true}
                R.id.bottomMap -> {presenter.doViewHillfortsMap()
                    true}
                else -> false
            }
        }
    }

    fun sendEmail(email: String, subject: String, message: String){

            // launch email client
            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            // set the email address you want to email too
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            //set the subject
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            // set the message
            mIntent.putExtra(Intent.EXTRA_TEXT, message)

            try {
                startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))

            } catch (e: Exception) {
                toast("Error sending email: " + e.message)
            }

        btnDeleteHillfort.visibility = View.VISIBLE
        shareBtn.visibility = View.VISIBLE
        emailAddress.visibility = View.INVISIBLE
        sendEmailBtn.visibility = View.INVISIBLE
    }

    override fun showHillfort(hillfort: HillfortModel) {
        dateVisited.setText(hillfort.date)
        hillfortName.setText(hillfort.name)
        hillfortDescription.setText(hillfort.description)
        hillfortNotes.setText(hillfort.notes)
        hillfortRating.rating = hillfort.rating
        if(hillfort.visited)
        {
            visitedHillfort.isChecked = true
            dateVisited.setText(hillfort.date)
        }
        if(hillfort.favourite)
        {
            hillfortFavourite.isChecked = true
        }
        this.showImages(hillfort.images)
        this.showLocation(hillfort.location)
        btnAdd.setText(R.string.save_hillfort)
    }

    override fun showLocation (loc : Location) {
        hillfortLat.setText("%.6f".format(loc.lat))
        hillfortLng.setText("%.6f".format(loc.lng))
    }

    //    Show the current images
    override fun showImages (images: ArrayList<String>) {
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
       if(presenter.edit) menu.findItem(R.id.item_delete).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_save ->{
             if(hillfortName.text.toString().isEmpty()){
                 toast(R.string.enter_hillfort_name)
             }else{
                 presenter.doAddOrSave(
                     hillfortName.text.toString(),
                     hillfortDescription.text.toString(),
                     hillfortNotes.text.toString(),
                     visitedHillfort.isChecked,
                     dateVisited.text.toString(),
                     hillfortRating.rating,
                     hillfortFavourite.isChecked
                     )
             }
            }
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.nav_sign_out ->{
                presenter.doLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onImageClick(image: String){
//        startActivityForResult(intentFor<ImageActivity>().putExtra("image", image),DELETE_IMAGE)
    }

//   Functions needed to return the user to the HillfortListView after the Up navigation is pressed
    override fun onPrepareSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onPrepareSupportNavigateUpTaskStack(builder)
        builder.editIntentAt(builder.intentCount - 1)
    }

    override fun supportShouldUpRecreateTask(targetIntent: Intent): Boolean {
        info("Hillfort: supportShouldUpRecreateTask")
        return true
    }


    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.visitedHillfort -> {
                    if (checked) {
                        val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
                        val currentDateAndTime: String = simpleDateFormat.format(Date())
                        presenter.doSetVisited(true, currentDateAndTime)
                        dateVisited.setText(currentDateAndTime)
                    } else {
                       presenter.doSetVisited(false, "")
                        dateVisited.setText("")
                    }
                }
            }
        }
    }

    override fun onBackPressed(){
        presenter.doCancel()
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
}
