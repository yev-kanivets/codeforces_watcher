package com.bogdan.codeforceswatcher.features.actions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.CFAction
import com.bogdan.codeforceswatcher.store
import kotlinx.android.synthetic.main.activity_action.*

class ActionActivity : AppCompatActivity() {

    private var cfAction: CFAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val commentId = intent.getLongExtra(COMMENT_ID, -1)
        cfAction = store.state.actions.actions.find { it.comment?.id == commentId }

        initViews()
    }

    private fun initViews() {
        val pageTitle = cfAction?.blogEntry?.title.orEmpty()
        val link = getString(R.string.comment_url, cfAction?.blogEntry?.id, cfAction?.comment?.id)
        tvPageTitle.text = pageTitle
        btnShare.setOnClickListener { share(link, pageTitle) }
        setupWebView(link)
    }

    private fun share(link: String, pageTitle: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        val shareText = "$pageTitle - $link\n\n${getString(R.string.shared_through_cw)}"
        share.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(share, getString(R.string.share_with_friends)))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        webView.loadUrl(url)

        webView.settings.javaScriptEnabled = true
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                webView.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBarHorizontal?.visibility = View.GONE
                webView?.visibility = View.VISIBLE
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KEYCODE_BACK -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val COMMENT_ID = "comment_id"

        fun newIntent(context: Context, commentId: Long): Intent {
            val intent = Intent(context, ActionActivity::class.java)
            intent.putExtra(COMMENT_ID, commentId)
            return intent
        }
    }
}