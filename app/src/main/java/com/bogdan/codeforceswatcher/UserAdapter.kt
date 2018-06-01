package com.bogdan.codeforceswatcher

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bogdan.codeforceswatcher.R.color.*

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
        Log.d("TAG", p.rank + "vs" + getColor(p.rank!!).toString())

        (view.findViewById<View>(R.id.tv1) as TextView).setTextColor(ctx.resources.getColor(getColor(p.rank)))
        (view.findViewById<View>(R.id.tv2) as TextView).setTextColor(ctx.resources.getColor(getColor(p.rank)))

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

}
