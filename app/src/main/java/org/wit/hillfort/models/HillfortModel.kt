package org.wit.hillfort.models
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import kotlin.collections.ArrayList

// Hillfort Model
@Parcelize
@Entity
@TypeConverters
data class HillfortModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                         var fbId: String ="",
                         var name: String = "",
                         var description: String="",
                         var images: ArrayList<String> = arrayListOf<String>(),
                         var visited: Boolean = false,
                         var notes: String ="",
                         var date: String ="",
                         var rating: Float = 0F,
                         var favourite: Boolean = false,
                          @Embedded var location: Location = Location()): Parcelable

// Map location model
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

