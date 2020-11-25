package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.*
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

    //    Return a list of all hillforts
    override fun findAll(userId: Long): MutableList<HillfortModel> {
        var foundHillforts:ArrayList<HillfortModel> = ArrayList<HillfortModel>()
        for(hillfort in hillforts)
        {
            if(hillfort.user == userId)
            {
                foundHillforts.add(hillfort.copy())
                info("$hillfort added to foundHillforts Array")
            }
        }
        return foundHillforts
    }

    //  Return one hillfort
    override fun findOne(hillfort: HillfortModel): HillfortModel{
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            return foundHillfort
        }
        return hillfort
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
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
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
        hillforts.remove(hillfort)
        serialize()
    }

    //  Delete an image from a hillfort
    override fun deleteImage(hillfort: HillfortModel, image: String) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        foundHillfort?.images?.remove(image)
        serialize()
    }

    // Delete all hillforts for a certain user
    override fun deleteUserHillforts(userId: Long) {
        val foundHillforts = findAll(userId)

        for(hillfort in foundHillforts)
        {
            if(hillfort.user == userId)
            {
                info("deleting hillfort for $userId")
                hillforts.remove(hillfort)
            }
            serialize()
        }
    }

//    Statistics

    //    Total number of hillforts a user has
    override fun totalHillforts(userId: Long): Int{
        val total = findAll(userId).size
        info("Total user hillforts: $total")
        return total
        }

    //    Total number of hillforts the user has viewed
    override fun viewedHillforts(userId: Long): Int{
        val foundHillforts = findAll(userId)
        var total = 0
        for (hillfort in foundHillforts)
        {
            if (hillfort.visited) total++
        }
        info("Average user visited: $total")
        return total
    }

    //  Total number of hillforts the user still has to view
    override fun unseenHillforts(userId:Long): Int{
        val total = totalHillforts(userId) - viewedHillforts(userId)
        info("Average user unseen: $total")
        return total
    }

    //  return the average number of hillforts per student
    override fun classAverageTotal(numOfUsers:Int): Int {
        val averageViewed = hillforts.size / numOfUsers
        return averageViewed
    }

    //   return the average number of hillforts viewed by the class
    override fun classAverageViewed(numOfUsers: Int):Int{
        var totalViewed = 0
        for (hillfort in hillforts)
        {
            if(hillfort.visited){
                totalViewed++
            }
        }
        var averageViewed = totalViewed/numOfUsers
        info("Average number of hillforts viewed in the class: $averageViewed")
        return averageViewed
    }

    //    Return the average number of hillforts the class still has to view
    override fun classAverageUnseen(numOfUsers: Int):Int{
        var totalUnseen = 0
        for (hillfort in hillforts)
        {
            if(!hillfort.visited){
                totalUnseen++
            }
        }
        var averageUnseen = totalUnseen / numOfUsers
        info("Average number of hillforts unseen in the class: $averageUnseen")
        return averageUnseen
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
}