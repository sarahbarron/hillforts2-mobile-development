package org.wit.hillfort.models.room
import android.content.Context
import androidx.room.Room
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hillfortDao()
    }

    override fun findAll(): List<HillfortModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): HillfortModel? {
        return dao.findById(id)
    }


    override fun create(hillfort: HillfortModel) {
        dao.create(hillfort)
    }

    override fun update(hillfort: HillfortModel) {
        dao.update(hillfort)
    }

    override fun delete(hillfort: HillfortModel) {
        dao.deleteHillfort(hillfort)
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


    override fun clear() {
        TODO("Not yet implemented")
    }

}