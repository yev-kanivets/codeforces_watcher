package com.bogdan.codeforceswatcher.features.contests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bogdan.codeforceswatcher.R
import kotlinx.android.synthetic.main.activity_contests_filters.*

class ContestsFiltersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contests_filters)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
