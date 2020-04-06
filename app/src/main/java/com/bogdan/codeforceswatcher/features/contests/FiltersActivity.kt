package com.bogdan.codeforceswatcher.features.contests

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.features.contests.models.Platform
import io.xorum.codeforceswatcher.redux.store
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
        val filters = store.state.contests.filters
        return listOf(
                FilterItem(R.drawable.codeforces, "Codeforces", Platform.CODEFORCES, filters.contains(Platform.CODEFORCES)),
                FilterItem(R.drawable.codeforces, "Codeforces Gym", Platform.CODEFORCES_GYM, filters.contains(Platform.CODEFORCES_GYM)),
                FilterItem(R.drawable.atcoder, "AtCoder", Platform.ATCODER, filters.contains(Platform.ATCODER)),
                FilterItem(R.drawable.leetcode, "LeetCode", Platform.LEETCODE, filters.contains(Platform.LEETCODE)),
                FilterItem(R.drawable.topcoder, "TopCoder", Platform.TOPCODER, filters.contains(Platform.TOPCODER)),
                FilterItem(R.drawable.csacademy, "CS Academy", Platform.CS_ACADEMY, filters.contains(Platform.CS_ACADEMY)),
                FilterItem(R.drawable.codechef, "CodeChef", Platform.CODECHEF, filters.contains(Platform.CODECHEF)),
                FilterItem(R.drawable.hackerrank, "HackerRank", Platform.HACKERRANK, filters.contains(Platform.HACKERRANK)),
                FilterItem(R.drawable.hackerearth, "HackerEarth", Platform.HACKEREARTH, filters.contains(Platform.HACKEREARTH)),
                FilterItem(R.drawable.kickstart, "Kick Start", Platform.KICK_START, filters.contains(Platform.KICK_START))
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
