package org.wit.hillfort.views.hillfort
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.wit.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortName
import org.jetbrains.anko.*
import org.wit.hillfort.views.image.ImageActivity
import org.wit.hillfort.activities.ImageAdapter
import org.wit.hillfort.activities.ImageListener
import org.wit.hillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HillfortView : BaseView(), AnkoLogger, ImageListener {

    lateinit var presenter: HillfortPresenter
    var hillfort = HillfortModel()
    val DELETE_IMAGE = 3
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)

        init(toolbarAdd)

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }

        setSupportActionBar(toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter

        chooseImage.setOnClickListener{
            presenter.cacheHillfort(hillfortName.text.toString(), hillfortDescription.text.toString())
            presenter.doSelectImage()
        }

        hillfortLocation.setOnClickListener{
            presenter.cacheHillfort(hillfortName.text.toString(), hillfortDescription.text.toString())
            presenter.doSetLocation()
        }

//        val layoutManager = LinearLayoutManager(this)
//        recyclerViewImages.layoutManager = layoutManager


        btnAdd.setOnClickListener() {
            if (hillfortName.text.toString().isEmpty()) {
                toast(R.string.enter_hillfort_name)
            }
            else{
                presenter.doAddOrSave(hillfortName.text.toString(), hillfortDescription.text.toString())
            }
        }
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortName.setText(hillfort.name)
        hillfortDescription.setText(hillfort.description)
        showImages(hillfort.images)
//        hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images[0]))
        if (hillfort.images.size > 0) {
            chooseImage.setText(R.string.change_hillfort_image)
        }
        btnAdd.setText(R.string.save_hillfort)
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
        if(presenter.edit) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_save ->{
             if(hillfortName.text.toString().isEmpty()){
                 toast(R.string.enter_hillfort_name)
             }else{
                 presenter.doAddOrSave(hillfortName.text.toString(), hillfortDescription.text.toString())
             }
            }
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//
//    //    retrieve the current hillfort from the JSON file
//    fun loadHillfort(){
//        hillfort = app.hillforts.findOne(hillfort.copy())
//        showImages(hillfort.images)
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onImageClick(image: String){
        startActivityForResult(intentFor<ImageActivity>().putExtra("image", image),DELETE_IMAGE)
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
                        presenter.doSetVisited(true)
                        val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
                        val currentDateAndTime: String = simpleDateFormat.format(Date())
                        hillfort.date = currentDateAndTime
                        dateVisited.setText("Date Visited: $currentDateAndTime")
                    } else {
                       presenter.doSetVisited(false)
                        hillfort.date = ""
                        dateVisited.setText("Date Visited: ")
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