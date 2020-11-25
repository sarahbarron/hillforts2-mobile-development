package org.wit.hillfort.models

interface HillfortStore {
    fun findAll(userId: Long): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun visited(hillfort: HillfortModel, boolean: Boolean)
    fun deleteImage(hillfort: HillfortModel, image: String)
    fun findOne(hillfort: HillfortModel): HillfortModel
    fun deleteUserHillforts(userId: Long)
    fun totalHillforts(userId: Long): Int
    fun viewedHillforts(userId: Long): Int
    fun unseenHillforts(userId:Long): Int
    fun classAverageTotal(numOfUsers: Int):Int
    fun classAverageViewed(numOfUsers:Int):Int
    fun classAverageUnseen(numOfUsers: Int):Int
}