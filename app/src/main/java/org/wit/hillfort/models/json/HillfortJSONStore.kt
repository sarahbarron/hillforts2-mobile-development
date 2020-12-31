package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*
import kotlin.collections.ArrayList

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

// Generate a random ID for each hillfort
fun generateRandomId(): Long {
    return Random().nextLong()
}


// Json store for hillforts
class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    //    Constructor checks if the file exists if it does it deserializes it
    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findById(id:Long) : HillfortModel? {
        val foundPlacemark: HillfortModel? = hillforts.find { it.id == id }
        return foundPlacemark
    }

    //    Return a list of all hillforts
    override fun findAll(): MutableList<HillfortModel> {
        var foundHillforts:ArrayList<HillfortModel> = ArrayList<HillfortModel>()
        for(hillfort in hillforts)
        {
                foundHillforts.add(hillfort.copy())
                info("$hillfort added to foundHillforts Array")
        }
        return foundHillforts
    }

    //  Return one hillfort
    //  Return one hillfort
    override fun findOne(hillfort: HillfortModel): HillfortModel{
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            return foundHillfort
        }
        return hillfort
    }

    override fun deleteUserHillforts() {
        TODO("Not yet implemented")
    }

    //    Create a hillfort
    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    // Update a hillfort
    override fun update(hillfort: HillfortModel) {

        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.location = hillfort.location
            serialize()
        }
    }

    // Check if a hillfort has been visited or not
    override fun visited(hillfort: HillfortModel, boolean: Boolean) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.visited = boolean
            serialize()
        }
    }

    // delete a hillfort
    override fun delete(hillfort: HillfortModel) {
        val foundHillfort: HillfortModel? = hillforts.find{it.id == hillfort.id}
        hillforts.remove(foundHillfort)
        serialize()
    }

    //  Delete an image from a hillfort
    override fun deleteImage(hillfort: HillfortModel, image: String) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        foundHillfort?.images?.remove(image)
        serialize()
    }


    // Serialize / write data to the JSON file
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    //    deserialize / read data from the JSON file
    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }

    override fun clear() {
        hillforts.clear()
    }

    override fun findFavourites(): List<HillfortModel> {
        val foundHillforts: List<HillfortModel> = hillforts.filter{ p -> p.favourite == true }
        return foundHillforts
    }

}