package org.wit.hillfort.views.image

import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

class ImagePresenter(view: BaseView):BasePresenter(view) {

    fun deleteImage(hillfort: HillfortModel, image: String){
        app.hillforts.deleteImage(hillfort.copy(), image)
    }
}