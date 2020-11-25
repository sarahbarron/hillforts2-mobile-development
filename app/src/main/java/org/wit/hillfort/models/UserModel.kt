package org.wit.hillfort.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// User Model
@Parcelize
data class UserModel(var id: Long = 0,
                     var username: String = "",
                     var password: String="", ): Parcelable
