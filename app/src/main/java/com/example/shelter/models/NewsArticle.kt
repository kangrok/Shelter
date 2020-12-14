package com.example.shelter.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class NewsArticle(
    val id: String,
    val title: String,
    val author: String,
    val authorImgUrl: String,
    val publishDate: Date,
    val imgUrl: String,
    val summary: String
) : Parcelable {
    var content: String = ""
}