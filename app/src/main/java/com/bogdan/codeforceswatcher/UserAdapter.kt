package com.bogdan.codeforceswatcher

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bogdan.codeforceswatcher.R.color.*
import com.bogdan.codeforceswatcher.R.drawable.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class UserAdapter internal constructor(private val ctx: Context, private var objects: MutableList<User>) : BaseAdapter() {

    private var lInflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): User {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.list_view, parent, false)
        }

        val p = getItem(position)

        (view!!.findViewById<View>(R.id.tv1) as TextView).text = p.handle
        if (p.rating == null) {
            (view.findViewById<View>(R.id.tv2) as TextView).text = ""
        } else
            (view.findViewById<View>(R.id.tv2) as TextView).text = p.rating.toString()
        if (p.rank == null) {
            (view.findViewById<View>(R.id.tv1) as TextView).setTextColor(ctx.resources.getColor(grey))
            (view.findViewById<View>(R.id.tv2) as TextView).setTextColor(ctx.resources.getColor(grey))
        } else {
            (view.findViewById<View>(R.id.tv1) as TextView).setTextColor(ctx.resources.getColor(getColor(p.rank)))
            (view.findViewById<View>(R.id.tv2) as TextView).setTextColor(ctx.resources.getColor(getColor(p.rank)))
        }
        if (p.ratingChanges.lastOrNull() != null) {
            (view.findViewById<View>(R.id.tv3) as TextView).text = getDataTime(p.ratingChanges.lastOrNull()!!.ratingUpdateTimeSeconds * 1000)
            if (p.ratingChanges.lastOrNull()!!.newRating - p.ratingChanges.lastOrNull()!!.oldRating >= 0) {
                (view.findViewById<View>(R.id.ivDelta) as ImageView).setImageResource(ic_rating_up)
                (view.findViewById<View>(R.id.tv4) as TextView).text = (p.ratingChanges.lastOrNull()!!.newRating - p.ratingChanges.lastOrNull()!!.oldRating).toString()
                (view.findViewById<View>(R.id.tv4) as TextView).setTextColor(ctx.resources.getColor(brightgreen))
            } else {
                (view.findViewById<View>(R.id.ivDelta) as ImageView).setImageResource(ic_rating_down)
                (view.findViewById<View>(R.id.tv4) as TextView).text = (p.ratingChanges.lastOrNull()!!.oldRating - p.ratingChanges.lastOrNull()!!.newRating).toString()
                (view.findViewById<View>(R.id.tv4) as TextView).setTextColor(ctx.resources.getColor(red))
            }
        } else {
            (view.findViewById<View>(R.id.tv3) as TextView).text = "No rating update"
        }

        return view
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
