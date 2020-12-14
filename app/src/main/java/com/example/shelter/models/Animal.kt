package com.example.shelter.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Animal(
    val id: String,
    val name: String,
    val type: String,
    val gender: String,
    val breed: String,
    val age: String,
    val ageUnit: String,
    val description: String,
    val imgLocations: ArrayList<String>
) : Parcelable {
    var imgs = arrayListOf<Uri>()

    fun addImg(uri: Uri) {
        imgs.add(uri)
        if (imgs.size > 1) {
            imgs.sort()
        }
    }
}