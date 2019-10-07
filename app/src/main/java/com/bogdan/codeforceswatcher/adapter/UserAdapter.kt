package com.bogdan.codeforceswatcher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.R.color.blue
import com.bogdan.codeforceswatcher.R.color.blue_green
import com.bogdan.codeforceswatcher.R.color.bright_green
import com.bogdan.codeforceswatcher.R.color.green
import com.bogdan.codeforceswatcher.R.color.grey
import com.bogdan.codeforceswatcher.R.color.orange
import com.bogdan.codeforceswatcher.R.color.purple
import com.bogdan.codeforceswatcher.R.color.red
import com.bogdan.codeforceswatcher.features.users.UserActivity
import com.bogdan.codeforceswatcher.features.users.models.User
import kotlinx.android.synthetic.main.users_list_view.view.ivDelta
import kotlinx.android.synthetic.main.users_list_view.view.tvHandle
import kotlinx.android.synthetic.main.users_list_view.view.tvLastRatingUpdate
import kotlinx.android.synthetic.main.users_list_view.view.tvRating
import kotlinx.android.synthetic.main.users_list_view.view.tvRatingChange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            LayoutInflater.from(context).inflate(R.layout.users_list_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.tvHandle.text = user.handle
        if (user.rating == null) {
            holder.tvRating.text = null
        } else holder.tvRating.text = user.rating.toString()
        if (user.rank == null) {
            holder.tvHandle.setTextColor(ContextCompat.getColor(context, grey))
            holder.tvRating.setTextColor(ContextCompat.getColor(context, grey))
        } else {
            if (user.rank == "legendary grandmaster") {
                val text =
                    "<font color=black>${user.handle[0]}</font><font color=red>${user.handle.subSequence(
                        1,
                        user.handle.lastIndex + 1
                    )}</font>"
                holder.tvHandle.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                holder.tvHandle.setTextColor(ContextCompat.getColor(context, getColor(user.rank)))
            }
            holder.tvRating.setTextColor(ContextCompat.getColor(context, getColor(user.rank)))

        }
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

    private fun getColor(rank: String): Int {
        return when (rank) {
            "newbie" -> grey
            "pupil" -> green
            "specialist" -> blue_green
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvHandle: TextView = view.tvHandle
        val tvRating: TextView = view.tvRating
        val tvLastRatingUpdate: TextView = view.tvLastRatingUpdate
        val tvRatingChange: TextView = view.tvRatingChange
        val ivDelta: ImageView = view.ivDelta
    }
}
