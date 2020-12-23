package org.wit.hillfort.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun findById(id: Long): HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }

    override fun create(hillfort: HillfortModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.location = hillfort.location
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)

    }

    override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun visited(hillfort: HillfortModel, boolean: Boolean) {
        TODO("Not yet implemented")
    }

    override fun deleteImage(hillfort: HillfortModel, image: String) {
        TODO("Not yet implemented")
    }

    override fun findOne(hillfort: HillfortModel): HillfortModel {
        TODO("Not yet implemented")
    }

    override fun deleteUserHillforts() {
        TODO("Not yet implemented")
    }

//    override fun totalHillforts(userId: Long): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun viewedHillforts(userId: Long): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun unseenHillforts(userId: Long): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun classAverageTotal(numOfUsers: Int): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun classAverageViewed(numOfUsers: Int): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun classAverageUnseen(numOfUsers: Int): Int {
//        TODO("Not yet implemented")
//    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }
}