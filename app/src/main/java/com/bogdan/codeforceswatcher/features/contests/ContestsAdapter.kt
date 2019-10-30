package com.bogdan.codeforceswatcher.features.contests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.contests.models.Contest
import kotlinx.android.synthetic.main.view_contest_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ContestsAdapter(
    private val context: Context,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ContestsAdapter.ViewHolder>() {

    private var items: List<Contest> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_contest_item, parent, false),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contest = items[position]
        with(holder) {
            tvContestName.text = contest.name
            tvContestTime.text = getDateTime(contest.time)
        }
    }

    fun setItems(contestList: List<Contest>) {
        items = contestList
        notifyDataSetChanged()
    }

    private fun getDateTime(seconds: Long): String {
        val dateFormat = SimpleDateFormat("kk:mm MMM d, EEEE", Locale.getDefault())
        return dateFormat.format(Date(seconds * 1000)).toString()
    }

    class ViewHolder(view: View, itemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val tvContestName: TextView = view.tvContestName
        val tvContestTime: TextView = view.tvContestTime
        private val ivAddToCalendar: ImageView = view.ivAddToCalendar

        init {
            ivAddToCalendar.setOnClickListener {
                itemClickListener.invoke(adapterPosition)
            }
        }
    }
}
