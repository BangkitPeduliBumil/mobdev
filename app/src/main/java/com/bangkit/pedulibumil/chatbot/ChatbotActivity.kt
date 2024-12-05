package com.bangkit.pedulibumil.chatbot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.MainActivity
import com.bangkit.pedulibumil.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatbotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        // WebView setup
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        // Set WebViewClient to handle navigation
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()

                // Check if the URL is external
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    // Open external links in a browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    true // Indicate we handled the URL
                } else {
                    false // Allow WebView to load the URL
                }
            }
        }

        // Load the chatbot URL
        webView.loadUrl("https://glowyguide.com/pedulibumil/chatbot.html")

        // Back Button FAB setup
        val fabBack: FloatingActionButton = findViewById(R.id.fabBack)
        fabBack.setOnClickListener {
            // Intent to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
