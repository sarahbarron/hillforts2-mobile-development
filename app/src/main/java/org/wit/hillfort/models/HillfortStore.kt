package org.wit.hillfort.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun visited(hillfort: HillfortModel, boolean: Boolean)
    fun deleteImage(hillfort: HillfortModel, image: String)
    fun findOne(hillfort: HillfortModel): HillfortModel
    fun deleteUserHillforts()
//    fun totalHillforts(userId: Long): Int
//    fun viewedHillforts(userId: Long): Int
//    fun unseenHillforts(userId:Long): Int
//    fun classAverageTotal(numOfUsers: Int):Int
//    fun classAverageViewed(numOfUsers:Int):Int
//    fun classAverageUnseen(numOfUsers: Int):IntArray
    fun findById(id:Long) : HillfortModel?
    fun clear()
}