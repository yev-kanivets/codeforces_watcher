package com.bogdan.codeforceswatcher.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.contests.models.Contest
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.view_contest_item.view.ivAddToCalendar
import kotlinx.android.synthetic.main.view_contest_item.view.tvContestName
import kotlinx.android.synthetic.main.view_contest_item.view.tvContestTime
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContestAdapter(
    private val context: Context
) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    private var items: List<Contest> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_contest_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contest = items[position]
        holder.tvContestName.text = contest.name
        holder.tvContestTime.text = getDateTime(contest.time)
        holder.ivAddToCalendar.setOnClickListener { addContestToCalendar(items[position]) }
    }

    private fun addContestToCalendar(contest: Contest) {
        val timeStart = getCalendarTime(contest.time)
        val timeEnd = getCalendarTime(contest.time + contest.duration)
        val encodeName = URLEncoder.encode(contest.name)
        val calendarEventLink =
            "$CALENDAR_LINK?action=TEMPLATE&text=$encodeName&dates=$timeStart/$timeEnd&details=$CODEFORCES_LINK"
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(calendarEventLink))
        try {
            context.startActivity(intent)
        } catch (error: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.google_calendar_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
        Analytics.logAddContestToCalendarEvent(contest.name)
    }

    fun setItems(contestList: List<Contest>) {
        items = contestList
        notifyDataSetChanged()
    }

    private fun getDateTime(seconds: Long): String {
        val dateFormat = SimpleDateFormat("kk:mm MMM d, EEEE", Locale.getDefault())
        return dateFormat.format(Date(seconds * 1000)).toString()
    }

    private fun getCalendarTime(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyyMMd'T'HHmmss", Locale.getDefault())
        return dateFormat.format(Date(time * 1000)).toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContestName: TextView = view.tvContestName
        val tvContestTime: TextView = view.tvContestTime
        val ivAddToCalendar: ImageView = view.ivAddToCalendar
    }

    companion object {
        private const val CALENDAR_LINK = "https://calendar.google.com/calendar/render"
        private const val CODEFORCES_LINK = "http://codeforces.com/contests"
    }
}
