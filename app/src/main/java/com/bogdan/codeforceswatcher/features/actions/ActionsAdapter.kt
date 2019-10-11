package com.bogdan.codeforceswatcher.features.actions

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.Action
import com.bogdan.codeforceswatcher.features.users.models.getColorTextByUserRank
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_action_item.view.*

class ActionsAdapter(
    private val context: Context,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ActionsAdapter.ViewHolder>() {

    private var items: List<Action> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_action_item, parent, false),
            itemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = items[position]
        if (action.comment == null) return

        holder.tvTitle.text = action.blogEntry.title

        val commentatorAvatar = action.comment.commentatorAvatar

        if (commentatorAvatar.startsWith("https:")) {
            Picasso.get().load(commentatorAvatar).into(holder.ivAvatar)
        } else {
            Picasso.get().load("https:$commentatorAvatar").into(holder.ivAvatar)
        }

        val title = convertFromHtml(action.blogEntry.title)
        val underlinedTitle = SpannableString(title)
        underlinedTitle.setSpan(UnderlineSpan(), 0, title.length, 0)
        holder.tvTitle.text = underlinedTitle

        val colorHandle = getColorTextByUserRank(
            action.comment.commentatorHandle, action.comment.commentatorRank, context
        )
        holder.tvHandle.text = TextUtils.concat(SpannableString(context.getString(R.string.commented_by)), colorHandle)

        val startTime = action.comment.creationTimeSeconds
        holder.tvTimeAgo.text = calculateDifferenceInTime(startTime * 1000, System.currentTimeMillis())

        holder.tvContent.text = convertFromHtml(action.comment.text)
    }

    private fun convertFromHtml(text: String) =
        HtmlCompat.fromHtml(text
            .replace("\n", "<br>")
            .replace("\t", "<tl>")
            .replace("$", ""),
            HtmlCompat.FROM_HTML_MODE_LEGACY)


    private fun calculateDifferenceInTime(startTime: Long, endTime: Long): String {
        val millisecondsInSecond = 1000
        val secondsInMinute = 60
        val minutesInHour = 60
        val hoursInDay = 24

        val differenceInSeconds = (endTime - startTime) / millisecondsInSecond
        val differenceInMinutes = differenceInSeconds / secondsInMinute
        val differenceInHours = differenceInMinutes / minutesInHour
        val differenceInDays = differenceInHours / hoursInDay

        return when {
            differenceInDays != 0L ->
                context.getString(R.string.days_ago, differenceInDays, getCorrectEndingDay(differenceInDays))
            differenceInHours != 0L ->
                context.getString(R.string.hours_ago, differenceInHours, getCorrectEndingHour(differenceInHours))
            differenceInMinutes != 0L ->
                context.getString(R.string.minutes_ago, differenceInMinutes, getCorrectEndingMinute(differenceInMinutes))
            else ->
                context.getString(R.string.seconds_ago, differenceInSeconds, getCorrectEndingSecond(differenceInSeconds))
        }
    }

    private fun getCorrectEndingDay(days: Long) =
        when {
            days % 100 in 10..19 -> context.getString(R.string.from_5_until_20_days_endings)
            days % 10 == 1L -> context.getString(R.string.one_day_ending)
            days % 10 in 2..4 -> context.getString(R.string.from_2_until_4_days_endings)
            else -> context.getString(R.string.from_5_until_20_days_endings)
        }


    private fun getCorrectEndingHour(hours: Long) =
        when {
            hours % 100 in 10..19 -> context.getString(R.string.from_5_until_20_hours_endings)
            hours % 10 == 1L -> context.getString(R.string.one_hour_ending)
            hours % 10 in 2..4 -> context.getString(R.string.from_2_until_4_hours_endings)
            else -> context.getString(R.string.from_5_until_20_hours_endings)
        }

    private fun getCorrectEndingMinute(minutes: Long) =
        when {
            minutes % 100 in 10..19 -> context.getString(R.string.from_5_until_20_minutes_endings)
            minutes % 10 == 1L -> context.getString(R.string.one_minute_ending)
            minutes % 10 in 2..4 -> context.getString(R.string.from_2_until_4_minutes_endings)
            else -> context.getString(R.string.from_5_until_20_minutes_endings)
        }

    private fun getCorrectEndingSecond(seconds: Long) =
        when {
            seconds % 100 in 10..19 -> context.getString(R.string.from_5_until_20_seconds_endings)
            seconds % 10 == 1L -> context.getString(R.string.one_second_ending)
            seconds % 10 in 2..4 -> context.getString(R.string.from_2_until_4_seconds_endings)
            else -> context.getString(R.string.from_5_until_20_seconds_endings)
        }

    fun setItems(actionsList: List<Action>) {
        items = actionsList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, itemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val tvHandle: TextView = view.tvHandle
        val tvTitle: TextView = view.tvTitle
        val tvTimeAgo: TextView = view.tvTimeAgo
        val tvContent: TextView = view.tvContent
        val ivAvatar: ImageView = view.ivAvatar

        init {
            view.setOnClickListener {
                itemClickListener.invoke(adapterPosition)
            }
        }
    }
}
