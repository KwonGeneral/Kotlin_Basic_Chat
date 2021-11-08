package com.example.kotlin_basic_chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        Log.d("TEST", "WebActivity WebActivity WebActivity")

        main_webview.visibility = View.GONE
        main_webview.apply {
            WebViewClient()
            settings.javaScriptEnabled = false
        }
        intent.data?.let { uri ->
            Log.d("TEST", "메인 URI -> $uri")
            main_webview.loadUrl(uri.toString())
        }

    }
}