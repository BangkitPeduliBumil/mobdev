package com.bangkit.pedulibumil.chatbot

import android.content.Intent
import android.os.Bundle
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
        webView.webViewClient = WebViewClient() // Keep navigation inside WebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        // Load URL
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
