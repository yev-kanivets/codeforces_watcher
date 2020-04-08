package com.bogdan.codeforceswatcher.features.actions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.ActionItem
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.convertFromHtml
import com.squareup.picasso.Picasso
import io.xorum.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import io.xorum.codeforceswatcher.redux.store
import kotlinx.android.synthetic.main.view_blog_entry_item.view.*
import kotlinx.android.synthetic.main.view_comment_item.view.*
import kotlinx.android.synthetic.main.view_comment_item.view.tvContent
import kotlinx.android.synthetic.main.view_comment_item.view.tvHandleAndTime
import kotlinx.android.synthetic.main.view_comment_item.view.tvTitle
import kotlinx.android.synthetic.main.view_pinned_action.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class ActionsAdapter(
        private val context: Context,
        private val itemClickListener: (String, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ActionItem> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                STUB_VIEW_TYPE -> {
                    val layout = LayoutInflater.from(context).inflate(R.layout.view_actions_stub, parent, false)
                    StubViewHolder(layout)
                }
                COMMENT_VIEW_TYPE -> {
                    val layout = LayoutInflater.from(context).inflate(R.layout.view_comment_item, parent, false)
                    CommentViewHolder(layout)
                }
                PINNED_ITEM_VIEW_TYPE -> {
                    val layout = LayoutInflater.from(context).inflate(R.layout.view_pinned_action, parent, false)
                    PinnedItemViewHolder(layout)
                }
                else -> {
                    val layout = LayoutInflater.from(context).inflate(R.layout.view_blog_entry_item, parent, false)
                    BlogEntryViewHolder(layout)
                }
            }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position] is ActionItem.Stub -> STUB_VIEW_TYPE
            items[position] is ActionItem.CommentItem -> COMMENT_VIEW_TYPE
            items[position] is ActionItem.PinnedItem -> PINNED_ITEM_VIEW_TYPE
            else -> BLOG_ENTRY_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ActionItem.Stub -> return
            is ActionItem.PinnedItem -> bindPinnedItem(viewHolder as PinnedItemViewHolder, item)
            is ActionItem.CommentItem -> bindComment(viewHolder as CommentViewHolder, item)
            is ActionItem.BlogEntryItem -> bindBlogEntry(viewHolder as BlogEntryViewHolder, item)
        }
    }

    private fun bindComment(viewHolder: CommentViewHolder, comment: ActionItem.CommentItem) = with(comment) {
        with(viewHolder) {
            tvTitle.text = title
            tvHandleAndTime.text = TextUtils.concat(commentatorHandle, " - ${PrettyTime().format(Date(time * 1000))}")
            tvContent.text = content
            onItemClickListener = {
                val cfAction = store.state.actions.actions[it]
                itemClickListener(cfAction.link, cfAction.blogEntry.title.convertFromHtml())
            }
        }

        Picasso.get().load(commentatorAvatar)
                .placeholder(R.drawable.no_avatar)
                .into(viewHolder.ivAvatar)
    }

    private fun bindBlogEntry(viewHolder: BlogEntryViewHolder, blogEntry: ActionItem.BlogEntryItem) = with(blogEntry) {
        with(viewHolder) {
            tvTitle.text = blogTitle
            tvHandleAndTime.text = TextUtils.concat(authorHandle, " - ${PrettyTime().format(Date(time * 1000))}")
            tvContent.text = CwApp.app.getString(R.string.created_or_updated_text)
            onItemClickListener = {
                val cfAction = store.state.actions.actions[it]
                itemClickListener(cfAction.link, blogEntry.blogTitle)
            }
        }

        Picasso.get().load(authorAvatar)
                .placeholder(R.drawable.no_avatar)
                .into(viewHolder.ivAvatar)
    }

    private fun bindPinnedItem(viewHolder: PinnedItemViewHolder, pinnedItem: ActionItem.PinnedItem) = with(pinnedItem) {
        with(viewHolder) {
            tvTitle.text = title
            onItemClickListener = {
                itemClickListener(pinnedItem.link, pinnedItem.title)
                Analytics.logPinnedPostOpened()
            }
            onCrossClickListener = {
                store.dispatch(ActionsRequests.RemovePinnedPost(pinnedItem.link))
                Analytics.logPinnedPostClosed()
            }
        }
    }

    fun setItems(actionsList: List<ActionItem>) {
        items = if (actionsList.isEmpty()) listOf(ActionItem.Stub)
        else actionsList
        notifyDataSetChanged()
    }

    class PinnedItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.tvTitle

        var onItemClickListener: (() -> Unit)? = null
        var onCrossClickListener: (() -> Unit)? = null

        init {
            view.setOnClickListener {
                onItemClickListener?.invoke()
            }
            view.ivCross.setOnClickListener {
                onCrossClickListener?.invoke()
            }
        }
    }

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHandleAndTime: TextView = view.tvHandleAndTime
        val tvTitle: TextView = view.tvTitle
        val tvContent: TextView = view.tvContent
        val ivAvatar: ImageView = view.ivCommentatorAvatar

        var onItemClickListener: ((Int) -> Unit)? = null

        init {
            view.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    class BlogEntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHandleAndTime: TextView = view.tvHandleAndTime
        val tvTitle: TextView = view.tvTitle
        val tvContent: TextView = view.tvContent
        val ivAvatar: ImageView = view.ivAuthorAvatar

        var onItemClickListener: ((Int) -> Unit)? = null

        init {
            view.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    data class StubViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val STUB_VIEW_TYPE = 0
        const val COMMENT_VIEW_TYPE = 1
        const val BLOG_ENTRY_VIEW_TYPE = 2
        const val PINNED_ITEM_VIEW_TYPE = 3
    }
}