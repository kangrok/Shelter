package com.example.shelter.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shelter.R
import com.example.shelter.models.NewsArticle
import com.koushikdutta.ion.Ion
import java.text.SimpleDateFormat
import java.util.*

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        newsAdapter = NewsAdapter(object : NewsAdapter.NewsAdapterListener {
            override fun onArticleClick(article: NewsArticle) {
                Log.i("NewsFragment", "Article clicked")
                displayArticlesFragment(view, article)
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_news)
        recyclerView.adapter = newsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        displayNews()

        return view
    }

    private fun displayNews() {
        val articles = arrayListOf<NewsArticle>()
        Ion.with(this)
            .load("https://services.postimees.ee/rest/v1/terms/465965/articles")
            .asJsonArray()
            .setCallback { _, result ->
                result.asJsonArray.forEach {
                    val articleJson = it.asJsonObject
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                    val author = articleJson.get("authors").asJsonArray.get(0).asJsonObject
                    val thumbnail = if (author.get("thumbnail").isJsonNull) {
                        "https://f11.pmo.ee/AUPuadt4zS8q5iPGin9551SzoH0=/155x155/smart/nginx/o/2016/11/24/6106027t1h5f3e.png"
                    } else {
                        author.get("thumbnail").asJsonObject.get("sources").asJsonObject
                            .get("square").asJsonObject.get("xsmall").asString
                    }
                    articles.add(
                        NewsArticle(
                            articleJson.get("id").asString,
                            articleJson.get("headline").asString,
                            author.get("name").asString,
                            thumbnail,
                            sdf.parse(articleJson.get("datePublished").asString),
                            articleJson.get("thumbnail").asJsonObject.get("sources").asJsonObject
                                .get("landscape").asJsonObject.get("medium").asString,
                            articleJson.get("articleLead").asJsonArray.get(0).asJsonObject.get("html").asString
                                .replace("<p>", "").replace("</p>", "")
                        )
                    )
                }
                viewModel.articles = articles.toTypedArray()
                newsAdapter.data = viewModel.articles
                newsAdapter.notifyDataSetChanged()
            }
    }

    private fun displayArticlesFragment(view: View, article: NewsArticle) {
        val action = NewsFragmentDirections.navigateToArticle(article)
        Navigation.findNavController(view).navigate(action)
    }
}