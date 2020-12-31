package org.wit.hillfort.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

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
            updateImages(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.name = hillfort.name
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            foundHillfort.location = hillfort.location
            foundHillfort.notes = hillfort.notes
            foundHillfort.visited = hillfort.visited
            foundHillfort.date = hillfort.date
            foundHillfort.rating = hillfort.rating
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
        updateImages(hillfort)

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
       db.child("users").child(userId).removeValue()
    }


    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) {
                    it.getValue<HillfortModel>(
                        HillfortModel::class.java
                    )
                }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImages(hillfort: HillfortModel) {
            for (image in hillfort.images) {
                val fileName = File(image)
                val imageName = fileName.getName()

                var imageRef = st.child(userId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, image)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            hillfort.images.remove(image)
                            hillfort.images.add(it.toString())
                            db.child("users").child(userId).child("hillforts").child(hillfort.fbId)
                                .setValue(hillfort)
                        }
                    }
                }
            }
    }
}