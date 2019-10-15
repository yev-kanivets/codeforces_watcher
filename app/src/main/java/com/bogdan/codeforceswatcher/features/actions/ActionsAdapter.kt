package com.bogdan.codeforceswatcher.features.actions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.ActionItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_action_item.view.*

class ActionsAdapter(
    private val context: Context,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ActionsAdapter.ViewHolder>() {

    private var items: List<ActionItem> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val actionItem = R.layout.view_action_item
        return ViewHolder(
            LayoutInflater.from(context).inflate(actionItem, parent, false), itemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actionItem = items[position]

        holder.tvTitle.text = actionItem.title
        Picasso.get().load(actionItem.commentatorAvatar).into(holder.ivAvatar)
        holder.tvTitle.text = actionItem.title
        holder.tvHandle.text = actionItem.commentatorHandle
        holder.tvTimeAgo.text = actionItem.timeAgo
        holder.tvContent.text = actionItem.content
    }

    fun setItems(actionsList: List<ActionItem>) {
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
