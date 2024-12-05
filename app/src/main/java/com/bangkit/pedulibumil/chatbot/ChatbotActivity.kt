package com.bangkit.pedulibumil.chatbot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.pedulibumil.MainActivity
import com.bangkit.pedulibumil.R

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

                // Handle PDF files with Google Viewer
                if (url.endsWith(".pdf")) {
                    try {
                        val googlePdfUrl = "https://docs.google.com/gview?embedded=true&url=$url"
                        view.loadUrl(googlePdfUrl)
                    } catch (e: Exception) {
                        // If Google Viewer fails, open with external app
                        openPdfExternally(url)
                    }
                    return true
                }

                // Handle other URLs
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                }

                return false
            }
        }

        // Load the chatbot URL
        webView.loadUrl("https://glowyguide.com/pedulibumil/chatbot.html")

        // Back Button setup
        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // Intent to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun openPdfExternally(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(url), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Show message if no app available to open PDF
            Toast.makeText(this, "Tidak ada aplikasi untuk membuka file PDF.", Toast.LENGTH_SHORT).show()
        }
    }
}
