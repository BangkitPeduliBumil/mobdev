package com.bangkit.test.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bangkit.test.databinding.FragmentChatbotBinding

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengakses WebView menggunakan View Binding
        val webView: WebView = binding.webView

        // Menentukan WebViewClient untuk menjaga agar URL tetap di dalam aplikasi
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false // Semua URL tetap dibuka di WebView
            }
        }

        // Pengaturan tambahan untuk WebView
        webView.settings.apply {
            javaScriptEnabled = true // Aktifkan JavaScript
            domStorageEnabled = true // Aktifkan DOM Storage untuk fitur modern
            setSupportZoom(true) // Izinkan zoom
            builtInZoomControls = true // Tampilkan kontrol zoom
            displayZoomControls = false // Sembunyikan kontrol zoom default
        }

        // Memuat URL chatbot
        webView.loadUrl("https://glowyguide.com/pedulibumil/chatbot.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
