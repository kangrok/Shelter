package com.example.shelter.ui.news

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.shelter.R
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.view.*
import java.text.SimpleDateFormat

class ArticleFragment: Fragment() {

    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)
        displayArticle(view)
        return view
    }

    private fun displayArticle(view: View) {
        val article = args.article

        Ion.with(this)
            .load("https://services.postimees.ee/rest/v1/articles/${article.id}")
            .asJsonObject()
            .setCallback { _, result ->
                val body = result.get("articleBody").asJsonArray
                body.forEach {
                    val element = it.asJsonObject
                    if (element.get("type").asString == "htmlElement")
                    article.content += element.get("html").asString

                    Picasso.get().load(article.imgUrl).into(view.imageview_article)
                    Picasso.get().load(article.authorImgUrl).into(view.imageview_author)
                    view.textview_article_content.text = Html.fromHtml(article.content)
                    view.textview_article_title.text = article.title
                    view.textview_summary.text = article.summary
                    view.textview_author.text = article.author

                    val sdfDay = SimpleDateFormat("dd")
                    val sdfYear = SimpleDateFormat("yyyy")

                    val dateText = "${sdfDay.format(article.publishDate)}${month()} ${sdfYear.format(article.publishDate)}"
                    view.textview_article_date.text = dateText
                }
            }
    }

    private fun month(): String {
        val sdfMonth = SimpleDateFormat("MM")

        return when (sdfMonth.format(args.article.publishDate)) {
            "01" -> resources.getString(R.string.january)
            "02" -> resources.getString(R.string.february)
            "03" -> resources.getString(R.string.march)
            "04" -> resources.getString(R.string.april)
            "05" -> resources.getString(R.string.may)
            "06" -> resources.getString(R.string.june)
            "07" -> resources.getString(R.string.july)
            "08" -> resources.getString(R.string.august)
            "09" -> resources.getString(R.string.september)
            "10" -> resources.getString(R.string.october)
            "11" -> resources.getString(R.string.november)
            else -> resources.getString(R.string.december)
        }
    }
}