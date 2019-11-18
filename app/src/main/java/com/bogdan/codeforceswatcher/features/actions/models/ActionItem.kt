package com.bogdan.codeforceswatcher.features.actions.models

import android.text.SpannableStringBuilder
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.models.colorTextByUserRank
import com.bogdan.codeforceswatcher.util.LinkValidator

sealed class ActionItem {

    class CommentItem(action: CFAction) : ActionItem() {

        var commentatorHandle: CharSequence
        var title: String
        var content: String
        var commentatorAvatar: String
        var creationTimeSeconds: Long

        init {
            val comment = action.comment ?: throw NullPointerException()
            commentatorAvatar = LinkValidator.avatar(comment.commentatorAvatar)
            commentatorHandle = buildHandle(
                comment.commentatorHandle, comment.commentatorRank
            )
            title = action.blogEntry.title
            creationTimeSeconds = action.timeInMillisecond
            content = comment.text
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

        var authorHandle: CharSequence
        var blogTitle: String
        var authorAvatar: String
        var time: Long

        init {
            with(action) {
                authorAvatar = LinkValidator.avatar(blogEntry.authorAvatar)
                authorHandle = colorTextByUserRank(blogEntry.authorHandle, blogEntry.authorRank)
                blogTitle = blogEntry.title
                time = timeInMillisecond
            }
        }
    }

    object Stub : ActionItem()
}