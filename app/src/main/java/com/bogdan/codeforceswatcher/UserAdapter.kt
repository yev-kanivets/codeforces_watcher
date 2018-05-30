package com.bogdan.codeforceswatcher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserAdapter internal constructor(ctx: Context, private var objects: MutableList<User>) : BaseAdapter() {

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

        return view
    }

}
