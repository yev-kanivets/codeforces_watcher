package com.bogdan.codeforceswatcher.features.users

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
import com.bogdan.codeforceswatcher.features.users.models.Update
import com.bogdan.codeforceswatcher.features.users.models.UserItem
import kotlinx.android.synthetic.main.view_user_item.view.*

class UserAdapter(
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var items: List<UserItem> = listOf()

    override fun getItemCount() = items.size

    fun setItems(userList: List<UserItem>) {
        items = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        val user = items[position]

        tvHandle.text = user.handle
        tvRating.text = user.rating
        tvDateLastRatingUpdate.text = user.dateOfLastRatingUpdate
        tvLastRatingUpdate.text = user.lastRatingUpdate

        showLastRatingUpdate(user.update, holder)

        itemView.setOnClickListener {
            context.startActivity(UserActivity.newIntent(context, user.id))
        }
    }

    private fun showLastRatingUpdate(update: Update, holder: ViewHolder) = with(holder) {
        when (update) {
            Update.NULL -> {
                ivDelta.setImageResource(0)
                tvLastRatingUpdate.text = null
            }
            Update.DECREASE -> {
                ivDelta.setImageResource(R.drawable.ic_rating_down)
                tvLastRatingUpdate.setTextColor(ContextCompat.getColor(context, red))
            }
            Update.INCREASE -> {
                ivDelta.setImageResource(R.drawable.ic_rating_up)
                tvLastRatingUpdate.setTextColor(ContextCompat.getColor(context, bright_green))
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHandle: TextView = view.tvHandle
        val tvRating: TextView = view.tvRating
        val tvDateLastRatingUpdate: TextView = view.tvDateLastRatingUpdate
        val tvLastRatingUpdate: TextView = view.tvLastRatingUpdate
        val ivDelta: ImageView = view.ivDelta
    }
}
