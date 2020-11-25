package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

// Activity to view an image & delete an image
class ImageActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp
    var user = UserModel()
    var image = ""
    var hillfort = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        toolbarImage.title = title
        setSupportActionBar(toolbarImage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        app = application as MainApp

        // retrieve the user from the intent
        if(intent.hasExtra("user")){
            user = intent.extras?.getParcelable("user")!!
        }

        // retrieve the image and hillfort from the intent
       if(intent.hasExtra("image") && intent.hasExtra("hillfort")) {

           image = intent.extras?.getString("image")!!
           hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort")!!

           // create & set the bitmap with the image
           var bitmap = readImageFromPath(this, image)
           singleHillfortImage.setImageBitmap(bitmap)

           // listener for click on the delete image button
           // delete the image
           btnImageDelete.setOnClickListener(){
                app.hillforts.deleteImage(hillfort.copy(), image)
                finish()
           }
       }
    }

    //  Functions needed to return the user and the hillfort to the HillfortListActivity after the Up navigation is pressed
    override fun onPrepareSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onPrepareSupportNavigateUpTaskStack(builder)
        builder.editIntentAt(builder.intentCount - 1)?.putExtra("hillfort_edit", hillfort)?.putExtra("user",user)
    }
    override fun supportShouldUpRecreateTask(targetIntent: Intent): Boolean {
        return true
    }
}