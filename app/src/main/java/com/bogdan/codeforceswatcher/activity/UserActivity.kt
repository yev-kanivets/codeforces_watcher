package com.bogdan.codeforceswatcher.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.util.CustomMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user.chart
import kotlinx.android.synthetic.main.activity_user.ivAvatar
import kotlinx.android.synthetic.main.activity_user.tvCurrentRating
import kotlinx.android.synthetic.main.activity_user.tvHandle
import kotlinx.android.synthetic.main.activity_user.tvMaxRating
import kotlinx.android.synthetic.main.activity_user.tvRank
import kotlinx.android.synthetic.main.activity_user.tvRatingChanges
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class UserActivity : AppCompatActivity() {

    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        userId = intent.getLongExtra(ID, -1)

        val user = CwApp.app.userDao.getById(userId)
        displayUser(user)
        if (user.ratingChanges.isNotEmpty()) {
            displayChart(user)
        } else {
            tvRatingChanges.text = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun displayUser(user: User) {
        if (user.rank == null) {
            tvRank.text = getString(R.string.rank, getString(R.string.none))
        } else {
            tvRank.text = getString(R.string.rank, user.rank)
        }
        if (user.rating == null) {
            tvCurrentRating.text = getString(R.string.cur_rating, getString(R.string.none))
        } else {
            tvCurrentRating.text = getString(R.string.cur_rating, user.rating.toString())
        }
        val handle = if (user.firstName == null && user.lastName == null) {
            getString(R.string.none)
        } else if (user.firstName == null) {
            user.lastName
        } else if (user.lastName == null) {
            user.firstName
        } else
            user.firstName + " " + user.lastName
        tvHandle.text = getString(R.string.name, handle)
        if (user.maxRating == null) {
            tvMaxRating.text = getString(R.string.max_rating, getString(R.string.none))
        } else {
            tvMaxRating.text = getString(R.string.max_rating, user.maxRating.toString())
        }
        if (user.avatar.substring(0, 6) != "https:") {
            Picasso.get().load("https:" + user.avatar).into(ivAvatar)
        } else {
            Picasso.get().load(user.avatar).into(ivAvatar)
        }
        title = user.handle
    }

    private fun displayChart(user: User) {
        val entries = mutableListOf<Entry>()
        val xAxis = chart.xAxis
        chart.setTouchEnabled(true)
        chart.markerView = CustomMarkerView(this, R.layout.chart)
        chart.isDragEnabled = true
        chart.axisRight.setDrawLabels(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawAxisLine(true)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 3

        xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            val dateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
            dateFormat.format(Date(value.toLong() * 1000)).toString()
        }

        for (element in user.ratingChanges) {
            val ratingUpdateTime = element.ratingUpdateTimeSeconds.toFloat()
            val newRating = element.newRating.toFloat()
            val data = element.contestName

            entries.add(Entry(ratingUpdateTime, newRating, data))
        }

        val lineDataSet = LineDataSet(entries, user.handle)
        lineDataSet.setDrawValues(false)
        chart.data = LineData(lineDataSet)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_try, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                CwApp.app.userDao.delete(CwApp.app.userDao.getById(userId))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val ID = "id"

        fun newIntent(context: Context, userId: Long): Intent {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(ID, userId)
            return intent
        }
    }
}
