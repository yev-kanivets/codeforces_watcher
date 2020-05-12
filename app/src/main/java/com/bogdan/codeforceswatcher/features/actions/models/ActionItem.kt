package com.bogdan.codeforceswatcher.features.actions.models

import android.text.SpannableStringBuilder
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.colorTextByUserRank
import com.bogdan.codeforceswatcher.util.convertFromHtml
import io.xorum.codeforceswatcher.features.actions.models.CFAction
import io.xorum.codeforceswatcher.network.responses.PinnedPost
import io.xorum.codeforceswatcher.util.FeedbackData
import io.xorum.codeforceswatcher.util.avatar

sealed class ActionItem {

    class CommentItem(action: CFAction) : ActionItem() {

        var commentatorHandle: CharSequence
        var title: String = action.blogEntry.title.convertFromHtml()
        var content: String
        var commentatorAvatar: String
        val time: Long = action.timeSeconds
        val link = action.link

        init {
            val comment = action.comment ?: throw NullPointerException()
            commentatorAvatar = avatar(comment.commentatorAvatar ?: throw IllegalStateException())
            commentatorHandle = buildHandle(comment.commentatorHandle, comment.commentatorRank)
            content = comment.text.convertFromHtml()
        }

        private fun buildHandle(handle: String, rank: String?): CharSequence {
            val colorHandle = colorTextByUserRank(handle, rank)
            val commentedByString = CwApp.app.getString(R.string.commented_by)
            val handlePosition = commentedByString.indexOf("%1\$s")

            return SpannableStringBuilder(commentedByString)
                    .replace(handlePosition, handlePosition + "%1\$s".length, colorHandle)
        }
    }

    class BlogEntryItem(action: CFAction) : ActionItem() {

        val authorHandle: CharSequence
        val blogTitle: String
        val authorAvatar: String
        val time: Long = action.timeSeconds
        val link = action.link

        init {
            with(action) {
                authorAvatar = avatar(blogEntry.authorAvatar ?: throw IllegalStateException())
                authorHandle = colorTextByUserRank(blogEntry.authorHandle, blogEntry.authorRank)
                blogTitle = blogEntry.title.convertFromHtml()
            }
        }
    }

    class PinnedItem(pinnedPost: PinnedPost) : ActionItem() {
        val title = pinnedPost.title
        val link = pinnedPost.link
    }

    class FeedbackItem(feedbackData: FeedbackData): ActionItem() {
        val textPositiveButton = feedbackData.textPositiveButton
        val textNegativeButton = feedbackData.textNegativeButton
        val textTitle = feedbackData.textTitle
        val positiveButtonClick = feedbackData.positiveButtonClick
        val negativeButtonClick = feedbackData.negativeButtonClick
        val neutralButtonClick = feedbackData.neutralButtonClick
    }

    object Stub : ActionItem()
}
