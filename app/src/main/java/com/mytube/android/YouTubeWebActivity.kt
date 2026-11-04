
package com.mytube.android

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity

class YouTubeWebActivity : ComponentActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = true
            allowFileAccess = false
            allowContentAccess = false
            mixedContentMode =
                android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val uri = request.url
                val host = uri.host?.lowercase() ?: return true

                if (host == "accounts.google.com") {
                    openExternal(uri)
                    return true
                }

                val isYouTube =
                    host == "youtube.com" ||
                    host.endsWith(".youtube.com") ||
                    host == "youtu.be" ||
                    host.endsWith(".youtube-nocookie.com")

                if (isYouTube && uri.scheme == "https") {
                    return false
                }

                if (uri.scheme == "https" || uri.scheme == "http") {
                    openExternal(uri)
                }

                return true
            }
        }

        setContentView(webView)

        val requestedUrl = intent.getStringExtra("url")
            ?: "https://m.youtube.com/"

        val uri = Uri.parse(requestedUrl)
        val host = uri.host?.lowercase() ?: ""

        val validYouTube =
            uri.scheme == "https" &&
            (host == "youtube.com" ||
             host.endsWith(".youtube.com") ||
             host == "youtu.be")

        webView.loadUrl(
            if (validYouTube) requestedUrl
            else "https://m.youtube.com/"
        )
    }

    private fun openExternal(uri: Uri) {
        runCatching {
            startActivity(
                Intent(Intent.ACTION_VIEW, uri)
            )
        }
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.stopLoading()
            webView.destroy()
        }
        super.onDestroy()
    }
}
