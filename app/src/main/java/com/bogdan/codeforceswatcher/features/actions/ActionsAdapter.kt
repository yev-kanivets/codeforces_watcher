package com.bogdan.codeforceswatcher.features.actions

import android.content.Context
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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ActionItem> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            STUB_VIEW_TYPE -> {
                val layout = LayoutInflater.from(context).inflate(R.layout.view_action_stub, parent, false)
                StubViewHolder(layout)
            }
            else -> {
                val layout = LayoutInflater.from(context).inflate(R.layout.view_action_item, parent, false)
                ActionViewHolder(layout, itemClickListener)
            }
        }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is ActionItem.Stub) STUB_VIEW_TYPE
        else ACTION_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items[position] is ActionItem.Stub) return
        with(items[position] as ActionItem.Action) {
            (holder as ActionViewHolder).apply {
                tvTitle.text = title
                tvHandle.text = commentatorHandle
                tvTimeAgo.text = timeAgo
                tvContent.text = content
            }

            Picasso.get().load(commentatorAvatar)
                .placeholder(R.drawable.no_avatar)
                .into(holder.ivAvatar)
        }
    }

    fun setItems(actionsList: List<ActionItem>) {
        items = if (actionsList.isEmpty()) listOf(ActionItem.Stub)
        else actionsList
        notifyDataSetChanged()
    }

    class ActionViewHolder(view: View, itemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
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

    data class StubViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val STUB_VIEW_TYPE = 0
        const val ACTION_VIEW_TYPE = 1
    }
}