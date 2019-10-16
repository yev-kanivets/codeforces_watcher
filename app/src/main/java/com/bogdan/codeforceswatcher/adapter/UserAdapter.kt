package com.bogdan.codeforceswatcher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.R.color.bright_green
import com.bogdan.codeforceswatcher.R.color.red
import com.bogdan.codeforceswatcher.features.users.UserActivity
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.features.users.models.colorTextByUserRank
import kotlinx.android.synthetic.main.view_user_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserAdapter(
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var items: List<User> = listOf()

    override fun getItemCount() = items.size

    fun setItems(userList: List<User>) {
        items = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.tvHandle.text = colorTextByUserRank(user.handle, user.rank, context)
        holder.tvRating.text = colorTextByUserRank(user.rating?.toString().orEmpty(), user.rank, context)

        val lastRatingChange = user.ratingChanges.lastOrNull()
        if (lastRatingChange != null) {
            val ratingDelta = lastRatingChange.newRating - lastRatingChange.oldRating
            holder.tvLastRatingUpdate.text = context.resources.getString(
                R.string.last_rating_update,
                getDateTime(lastRatingChange.ratingUpdateTimeSeconds)
            )
            if (ratingDelta >= 0) {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_up)
                holder.tvRatingChange.text = ratingDelta.toString()
                holder.tvRatingChange.setTextColor(ContextCompat.getColor(context, bright_green))
            } else {
                holder.ivDelta.setImageResource(R.drawable.ic_rating_down)
                holder.tvRatingChange.text = (-ratingDelta).toString()
                holder.tvRatingChange.setTextColor(ContextCompat.getColor(context, red))
            }
        } else {
            holder.tvLastRatingUpdate.text = context.resources.getString(R.string.no_rating_update)
            holder.ivDelta.setImageResource(0)
            holder.tvRatingChange.text = null
        }

        holder.itemView.setOnClickListener {
            context.startActivity(UserActivity.newIntent(context, user.id))
        }
    }

    private fun getDateTime(seconds: Long): String {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return dateFormat.format(Date(seconds * 1000)).toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHandle: TextView = view.tvHandle
        val tvRating: TextView = view.tvRating
        val tvLastRatingUpdate: TextView = view.tvLastRatingUpdate
        val tvRatingChange: TextView = view.tvRatingChange
        val ivDelta: ImageView = view.ivDelta
    }
}
