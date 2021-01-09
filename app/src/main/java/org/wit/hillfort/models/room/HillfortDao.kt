package org.wit.hillfort.models.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.*
import org.wit.hillfort.models.HillfortModel

@Dao
@TypeConverters
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Query("select * from HillfortModel where id = :id")
    fun findById(id: Long): HillfortModel

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findFavourites(): List<HillfortModel>

    @Query("SELECT * FROM HillfortModel where name=:text")
    fun search(text: String?): List<HillfortModel>

//    @Update
//    fun visited(hillfort: HillfortModel, visited: Boolean, date: String)

}