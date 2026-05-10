package com.myapp.repositorysearcher.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

// webviewで実装

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(
    url: String,
    goBackSearchScreen: () -> Unit
) {
    val context = LocalContext.current
    var onBack by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var pendingUrl by remember { mutableStateOf("") }

    BackHandler(enabled = true, onBack = { onBack = true })

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val clickedUrl = request?.url?.toString() ?: return false

                        return if (clickedUrl.contains("github.com")) { // githubドメインなら継続処理
                            false
                        } else {
                            pendingUrl = clickedUrl
                            showAlert = true
                            true
                        }
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }, update = { webView ->
            when {
                onBack && webView.canGoBack() -> {
                    onBack = false
                    webView.goBack()
                }
                onBack && !webView.canGoBack() -> {
                    goBackSearchScreen()
                }
            }
        })
    OpenBrowserAlert(
        showAlert,
        { showAlert = it },
        pendingUrl,
        context
    )
}

@Composable
fun OpenBrowserAlert(
    showAlert: Boolean,
    onAlertChange: (Boolean) -> Unit,
    pendingUrl: String,
    context: Context
) {
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { onAlertChange(false) },
            title = { Text("外部サイトへ移動") },
            text = { Text("外部ブラウザでリンクを開きますか？\n$pendingUrl") },
            confirmButton = {
                TextButton(
                    onClick = {
                        openBrowser(pendingUrl, context)
                        onAlertChange(false)
                    })
                {
                    Text("開く")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAlertChange(false) })
                {
                    Text("キャンセル")
                }
            }
        )
    }
}

fun openBrowser(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}