package com.example.shelter

import android.R
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.shelter.R.*

class NewsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_news)

        val webview = WebView(this)
        webview.webViewClient = WebViewClient()
        webview.loadUrl("https://lemmik.elu24.ee/")
    }
}