package org.wit.hillfort.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import kotlin.collections.ArrayList

// Hillfort Model
@Parcelize
data class HillfortModel(var id: Long = 0,
                         var name: String = "",
                         var description: String="",
                         var images: ArrayList<String> = ArrayList<String>(),
                         var lat: Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f,
                         var visited: Boolean = false,
                         var notes: String ="",
                         var date: String="",
                         var user: Long = 0
                         ): Parcelable

// Map location model
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

