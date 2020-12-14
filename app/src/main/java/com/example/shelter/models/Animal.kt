package com.example.shelter.models

import android.graphics.Bitmap
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
    var imgs = arrayListOf<Bitmap>()

    fun addImg(img: Bitmap) {
        imgs.add(img)
        if (imgs.size > 1) {
            imgs.sortBy { it.byteCount }
        }
    }
}