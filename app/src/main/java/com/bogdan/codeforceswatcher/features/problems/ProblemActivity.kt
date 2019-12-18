package com.bogdan.codeforceswatcher.features.problems

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.activity_web_page.*

class ProblemActivity : AppCompatActivity() {

    private lateinit var pageTitle: String
    private lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        initData()
        initViews()

        Analytics.logProblemOpened()
    }

    private fun initData() {
        val problem = intent.getSerializableExtra(PROBLEM_ID) as Problem

        pageTitle = getString(R.string.problem_name_with_index, problem.contestId, problem.index, problem.name)
        link = buildPageLink(problem)
    }

    private fun buildPageLink(problem: Problem) =
        getString(R.string.problem_url, problem.contestId, problem.index)

    private fun initViews() {
        title = pageTitle
        setupWebView()
        swipeRefreshLayout.setOnRefreshListener { webView.reload() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                share()
                Analytics.logShareComment()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
        val shareText = "$pageTitle - $link\n\n${getString(R.string.shared_through_cw)}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_with_friends)))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() = with(webView) {
        loadUrl(link)
        settings.apply {
            javaScriptEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
        }
        webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBarHorizontal?.visibility = View.GONE
                webView?.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
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
        private const val PROBLEM_ID = "problem_id"

        fun newIntent(context: Context, problem: Problem): Intent {
            val intent = Intent(context, ProblemActivity::class.java)
            intent.putExtra(PROBLEM_ID, problem)
            return intent
        }
    }
}