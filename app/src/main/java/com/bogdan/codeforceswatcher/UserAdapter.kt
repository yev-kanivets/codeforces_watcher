package com.bogdan.codeforceswatcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
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
    return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.list_view, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val p = items[position]
    holder.tvHandle.text = p.handle
    if (p.rating == null) {
      holder.tvRating.text = null
    } else
      holder.tvRating.text = p.rating.toString()
    if (p.rank == null) {
      holder.tvHandle.setTextColor(ctx.resources.getColor(grey))
      holder.tvRating.setTextColor(ctx.resources.getColor(grey))
    } else {
      holder.tvHandle.setTextColor(ctx.resources.getColor(getColor(p.rank)))
      holder.tvRating.setTextColor(ctx.resources.getColor(getColor(p.rank)))
    }
    val lastRatingChange = p.ratingChanges.lastOrNull()
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
  val tvHandle = view.tv1
  val tvRating = view.tv2
  val tvLastRatingUpdate = view.tv3
  val tvRatingChange = view.tv4
  val ivDelta = view.ivDelta

}