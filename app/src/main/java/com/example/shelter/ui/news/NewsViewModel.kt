package com.example.shelter.ui.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shelter.models.NewsArticle

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    var articles: Array<NewsArticle> = arrayOf()
}