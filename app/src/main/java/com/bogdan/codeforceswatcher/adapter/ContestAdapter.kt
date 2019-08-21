package com.bogdan.codeforceswatcher.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.Contest
import kotlinx.android.synthetic.main.contests_list_view.view.*
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class ContestAdapter(private var items: List<Contest>, private val ctx: Context) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.contests_list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contest = items[position]
        holder.tvContestName.text = contest.name
        holder.tvContestTime.text = getDateTime(contest.time)
        holder.ivAddToCalendar.setOnClickListener { addContestToCalendar(items[position]) }
    }

    private fun addContestToCalendar(contest: Contest) {
        val timeStart = getCalendarTime(contest.time)
        val encodedName = URLEncoder.encode(
                contest.name,
                java.nio.charset.StandardCharsets.UTF_8.toString()
        ).toString()
        val timeEnd = getCalendarTime(contest.time + contest.duration)
        val calendarEventLink = "$CALENDAR_LINK?action=TEMPLATE&text=$encodedName&dates=$timeStart/$timeEnd&details=$CODEFORCES_LINK"
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(calendarEventLink))
        ctx.startActivity(intent)
    }

    fun setItems(contestList: List<Contest>) {
        items = contestList
        notifyDataSetChanged()
    }

    private fun getDateTime(seconds: Long): String {
        return SimpleDateFormat("kk:mm MMM d, yyyy", Locale.ENGLISH).format(Date(seconds * 1000)).toString()
    }

    private fun getCalendarTime(time: Long): String {
        return SimpleDateFormat("yyyyMMd'T'HHmmss", Locale.ENGLISH).format(Date(time * 1000)).toString()
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