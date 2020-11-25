package org.wit.hillfort.models
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L
internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    // find all hillforts for a user
    override fun findAll(userId: Long): List<HillfortModel> {
        return hillforts
    }

    // find one hillfort in the hillforts array
    override fun findOne(hillfort: HillfortModel): HillfortModel {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            return foundHillfort
        }
        return hillfort
    }

    // create a hillfort
    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    // update a hillfort
    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll()
        }
    }

    // check if a hillfort has been visited by a student
    override fun visited(hillfort: HillfortModel, boolean: Boolean) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.visited = boolean
        }
    }

    // delete a hillfort
    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    // delete all hillforts belonging to a user
    override fun deleteUserHillforts(userId: Long) {
        for (hillfort in hillforts) {
            if (hillfort.user == userId) {
                hillforts.remove(hillfort.copy())
            }
        }
    }

    // log all hillforts
    fun logAll() {
        hillforts.forEach { info("${it}") }
    }

    // delete an image
    override fun deleteImage(hillfort: HillfortModel, image: String) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        foundHillfort?.images?.remove(image)
    }

    //    Statistics

    //    return the otal number of hillforts a user has
    override fun totalHillforts(userId: Long): Int {
        val total = findAll(userId).size
        info("Total user hillforts: $total")
        return total
    }

    //  return the total number of hillforts the user has viewed
    override fun viewedHillforts(userId: Long): Int {
        val foundHillforts = findAll(userId)
        var total = 0
        for (hillfort in foundHillforts) {
            if (hillfort.visited) {
                total++
            }
        }
        info("Average user visited: $total")
        return total
    }

    //  return the total number of hillforts the user still has to view
    override fun unseenHillforts(userId: Long): Int {
        val total = totalHillforts(userId) - viewedHillforts(userId)
        info("Average user unseen: $total")
        return total
    }

    //   return the average number of hillforts each student has
    override fun classAverageTotal(numOfUsers: Int): Int {
        val averageViewed = hillforts.size / numOfUsers
        return averageViewed
    }

    // return the average number of hillforts each student has viewed
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

    //  Return the average number of hillforts the class still has to view
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
}