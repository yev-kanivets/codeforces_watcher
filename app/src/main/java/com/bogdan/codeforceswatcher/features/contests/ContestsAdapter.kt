package com.bogdan.codeforceswatcher.features.contests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.features.contests.models.Platform
import kotlinx.android.synthetic.main.view_contest_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ContestsAdapter(
        private val context: Context,
        private val addToCalendarClickListener: (Contest) -> Unit,
        private val itemClickListener: (Contest) -> Unit
) : RecyclerView.Adapter<ContestsAdapter.ViewHolder>() {

    private var items: List<Contest> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_contest_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contest = items[position]
        with(holder) {
            tvContestName.text = contest.name
            tvContestTime.text = getDateTime(contest.startTimeSeconds)
            ivContest.setImageResource(when (contest.platform) {
                Platform.ATCODER -> R.drawable.atcoder
                Platform.TOPCODER -> R.drawable.topcoder
                Platform.CODEFORCES -> R.drawable.codeforces
                Platform.CODECHEF -> R.drawable.codechef
                Platform.CODEFORCES_GYM -> R.drawable.codeforces
                Platform.LEETCODE -> R.drawable.leetcode
                Platform.KICK_START -> R.drawable.kickstart
                Platform.HACKEREARTH -> R.drawable.hackerearth
                Platform.HACKERRANK -> R.drawable.hackerrank
                Platform.CS_ACADEMY -> R.drawable.csacademy
            })

            onAddToCalendarClickListener = { addToCalendarClickListener(contest) }
            onItemClickListener = { itemClickListener(contest) }
        }
    }

    fun setItems(contestList: List<Contest>) {
        items = contestList
        notifyDataSetChanged()
    }

    private fun getDateTime(seconds: Long): String {
        val dateFormat = SimpleDateFormat(context.getString(R.string.contest_date_format), Locale.getDefault())
        return dateFormat.format(Date(seconds)).toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContestName: TextView = view.tvContestName
        val tvContestTime: TextView = view.tvContestTime
        val ivContest: ImageView = view.ivContest
        private val ivAddToCalendar: ImageView = view.ivAddToCalendar

        var onAddToCalendarClickListener: (() -> Unit)? = null
        var onItemClickListener: (() -> Unit)? = null

        init {
            ivAddToCalendar.setOnClickListener { onAddToCalendarClickListener?.invoke() }
            view.setOnClickListener { onItemClickListener?.invoke() }
        }
    }
}
