package com.bogdan.codeforceswatcher.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.Contest
import kotlinx.android.synthetic.main.contests_list_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class ContestAdapter(private var items: List<Contest>, private val ctx: Context) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.contests_list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvContestName.text = items[position].name
        holder.tvContestTime.text = getDataTime(items[position].time)
    }

    fun setItems(contestList: List<Contest>) {
        items = contestList
        notifyDataSetChanged()
    }

    private fun getDataTime(seconds: Long): String {
        return SimpleDateFormat("kk:mm MMM d, yyyy", Locale.ENGLISH).format(Date(seconds * 1000)).toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContestName: TextView = view.tvContestName
        val tvContestTime: TextView = view.tvContestTime
    }

}