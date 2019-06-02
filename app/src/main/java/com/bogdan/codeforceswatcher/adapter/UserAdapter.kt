package com.bogdan.codeforceswatcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.R.color.*
import com.bogdan.codeforceswatcher.activity.TryActivity
import com.bogdan.codeforceswatcher.model.User
import kotlinx.android.synthetic.main.list_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.Html


@Suppress("DEPRECATION")
class UserAdapter(private var items: List<User>, private val ctx: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(userList: List<User>) {
        items = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.tvHandle.text = user.handle
        if (user.rating == null) {
            holder.tvRating.text = null
        } else holder.tvRating.text = user.rating.toString()
        if (user.rank == null) {
            holder.tvHandle.setTextColor(ctx.resources.getColor(grey))
            holder.tvRating.setTextColor(ctx.resources.getColor(grey))
        } else {
            if (user.rank == "legendary grandmaster") {
                val text = "<font color=black>${user.handle[0]}</font><font color=red>${user.handle.subSequence(1, user.handle.lastIndex + 1)}</font>"
                holder.tvHandle.text = Html.fromHtml(text)
            } else {
                holder.tvHandle.setTextColor(ctx.resources.getColor(getColor(user.rank)))
            }
            holder.tvRating.setTextColor(ctx.resources.getColor(getColor(user.rank)))

        }
        val lastRatingChange = user.ratingChanges.lastOrNull()
        if (lastRatingChange != null) {
            val ratingDelta = lastRatingChange.newRating - lastRatingChange.oldRating
            holder.tvLastRatingUpdate.text = ctx.resources.getString(
                    R.string.last_rating_update,
                    getDataTime(lastRatingChange.ratingUpdateTimeSeconds * 1000)
            )
            if (ratingDelta >= 0) {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_up)
                holder.tvRatingChange.text = ratingDelta.toString()
                holder.tvRatingChange.setTextColor(ctx.resources.getColor(brightgreen))
            } else {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_down)
                holder.tvRatingChange.text = (-ratingDelta).toString()
                holder.tvRatingChange.setTextColor(ctx.resources.getColor(red))
            }
        } else {
            holder.tvLastRatingUpdate.text = ctx.resources.getString(R.string.no_rating_update)
            holder.ivDelta.setImageResource(0)
            holder.tvRatingChange.text = null
        }

        holder.itemView.setOnClickListener {
            ctx.startActivity(TryActivity.newIntent(ctx, user.id))
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun getDataTime(seconds: Long): String {
        return SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(seconds)).toString()
    }

    private fun getColor(rank: String): Int {
        return when (rank) {
            "newbie" -> grey
            "pupil" -> green
            "specialist" -> bluegreen
            "expert" -> blue
            "candidate master" -> purple
            "master" -> orange
            "international master" -> orange
            "grandmaster" -> red
            "international grandmaster" -> red
            "legendary grandmaster" -> red
            else -> grey
        }
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tvHandle: TextView = view.tvHandle
    val tvRating: TextView = view.tvRating
    val tvLastRatingUpdate: TextView = view.tvLastRatingUpdate
    val tvRatingChange: TextView = view.tvRatingChange
    val ivDelta: ImageView = view.ivDelta

}