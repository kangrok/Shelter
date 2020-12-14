package com.example.shelter.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shelter.R
import com.example.shelter.models.NewsArticle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_item.view.*
import java.util.*

class NewsAdapter(private var listener: NewsAdapterListener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    interface NewsAdapterListener {
        fun onArticleClick(article: NewsArticle)
    }

    var data = arrayOf<NewsArticle>()

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article: NewsArticle = data[position]

        holder.itemView.apply {
            if (article.imgUrl.isNotEmpty()) {
                Picasso.get().load(article.imgUrl).into(imageview_article)
            }
            if (article.authorImgUrl.isNotEmpty()) {
                Picasso.get().load(article.authorImgUrl).into(imageview_author)
            }
            textview_author.text = article.author
            textview_title.text = article.title
            textview_time.text = formatArticleTime(article.publishDate, this)
            setOnClickListener { listener.onArticleClick(article) }
        }
    }

    private fun formatArticleTime(date: Date?, view: View): String {
        if (date == null) return ""

        val time = getTimeUnit(date)
        return "${time.first} ${view.context.resources.getString(time.second)}"
    }

    private fun getTimeUnit(date: Date): Pair<Long, Int> {
        var time = (Calendar.getInstance().timeInMillis - date.time) / 60000
        if (time == 1L) return Pair(time, R.string.minute)
        if (time < 60) return Pair(time, R.string.minutes)

        time /= 60
        if (time == 1L) return Pair(time, R.string.hour)
        if (time < 24) return Pair(time, R.string.hours)

        time /= 24
        if (time == 1L) return Pair(time, R.string.day)
        if (time < 30) return Pair(time, R.string.days)

        time /= 30
        if (time == 1L) return Pair(time, R.string.month)
        if (time < 12) return Pair(time, R.string.months)

        time /= 12
        if (time == 1L) return Pair(time, R.string.year)
        return Pair(time, R.string.years)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}