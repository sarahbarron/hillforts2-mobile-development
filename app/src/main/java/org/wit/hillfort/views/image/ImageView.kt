package org.wit.hillfort.views.image

import android.content.Intent
import android.os.Bundle
import androidx.core.app.TaskStackBuilder
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

// Activity to view an image & delete an image
class ImageView: BaseView(), AnkoLogger {

    lateinit var presenter: ImagePresenter
    var image = ""
    var hillfort = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        init(toolbarImage,true)

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
                presenter.deleteImage(hillfort, image)
                finish()
            }
        }
    }

    //  Functions needed to return the user and the hillfort to the HillfortListView after the Up navigation is pressed
    override fun onPrepareSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onPrepareSupportNavigateUpTaskStack(builder)
        builder.editIntentAt(builder.intentCount - 1)?.putExtra("hillfort_edit", hillfort)
    }
    override fun supportShouldUpRecreateTask(targetIntent: Intent): Boolean {
        return true
    }
}