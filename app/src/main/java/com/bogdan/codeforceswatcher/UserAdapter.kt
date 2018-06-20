package com.bogdan.codeforceswatcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdan.codeforceswatcher.R.color.*
import com.bogdan.codeforceswatcher.activity.TryActivity
import kotlinx.android.synthetic.main.list_view.view.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class UserAdapter(val items: MutableList<User>, val ctx: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("MyHog", "String")
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = items[position]
        Log.d("MyHog", p.toString())
        holder.tv1.text = p.handle
        if (p.rating == null) {
            holder.tv2.text = null
        } else
            holder.tv2.text = p.rating.toString()
        if (p.rank == null) {
            holder.tv1.setTextColor(ctx.resources.getColor(grey))
            holder.tv2.setTextColor(ctx.resources.getColor(grey))
        } else {
            holder.tv1.setTextColor(ctx.resources.getColor(getColor(p.rank)))
            holder.tv2.setTextColor(ctx.resources.getColor(getColor(p.rank)))
        }
        val lastRatingChange = p.ratingChanges.lastOrNull()
        if (lastRatingChange != null) {
            val ratingDelta = lastRatingChange.newRating - lastRatingChange.oldRating
            holder.tv3.text = ctx.resources.getString(R.string.lastRatingUpdate, getDataTime(lastRatingChange.ratingUpdateTimeSeconds * 1000))
            if (ratingDelta >= 0) {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_up)
                holder.tv4.text = ratingDelta.toString()
                holder.tv4.setTextColor(ctx.resources.getColor(brightgreen))
            } else {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_down)
                holder.tv4.text = (-ratingDelta).toString()
                holder.tv4.setTextColor(ctx.resources.getColor(red))
            }
        } else {
            holder.tv3.text = ctx.resources.getString(R.string.noratingupdate)
            holder.ivDelta.setImageResource(0)
            holder.tv4.text = null
        }

        holder.itemView.setOnClickListener {
            Log.d("MyTag", p.id.toString())
            val intent = Intent(ctx, TryActivity::class.java)
            intent.putExtra("Handle", p.handle)
            intent.putExtra("Id", p.id.toString())
            ctx.startActivity(intent)
        }
    }

    private fun getColor(rank: String): Int {
        return when (rank) {
            "newbie" -> grey
            "pupil" -> green
            "specialist" -> bluegreen
            "expert" -> blue
            "master candidate" -> purple
            "master" -> orange
            "international master" -> orange
            "grandmaster" -> red
            "international grandmaster" -> red
            "legendary grandmaster" -> red
            else -> 1
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataTime(seconds: Long): String {
        return SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(seconds)).toString()
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // Holds the TextView that will add each animal to
    val tv1 = view.tv1
    val tv2 = view.tv2
    val tv3 = view.tv3
    val tv4 = view.tv4
    val ivDelta = view.ivDelta

}