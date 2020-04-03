package com.bogdan.codeforceswatcher.features.contests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.features.contests.models.Platform
import kotlinx.android.synthetic.main.activity_filters.*

class FiltersActivity : AppCompatActivity() {

    private val filtersAdapter: FiltersAdapter = FiltersAdapter(this, buildFiltersList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        recyclerView.adapter = filtersAdapter
    }

    private fun buildFiltersList(): List<FilterItem> {
        return listOf(
                FilterItem(R.drawable.codeforces, "Codeforces", Platform.CODEFORCES, false),
                FilterItem(R.drawable.codeforces, "Codeforces Gym", Platform.CODEFORCES_GYM, false),
                FilterItem(R.drawable.atcoder, "AtCoder", Platform.ATCODER, false),
                FilterItem(R.drawable.leetcode, "LeetCode", Platform.LEETCODE, false),
                FilterItem(R.drawable.topcoder, "TopCoder", Platform.TOPCODER, false),
                FilterItem(R.drawable.csacademy, "CS Academy", Platform.CS_ACADEMY, false),
                FilterItem(R.drawable.codechef, "CodeChef", Platform.CODECHEF, false),
                FilterItem(R.drawable.hackerrank, "HackerRank", Platform.HACKERRANK, false),
                FilterItem(R.drawable.hackerearth, "HackerEarth", Platform.HACKEREARTH, false),
                FilterItem(R.drawable.kickstart, "Kick Start", Platform.KICK_START, false)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
