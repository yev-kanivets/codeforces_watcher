package com.bogdan.codeforceswatcher.features.actions.models

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import androidx.core.text.HtmlCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.models.colorTextByUserRank
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class ActionItem(action: CFAction) {

    lateinit var commentatorHandle: CharSequence
    lateinit var title: CharSequence
    lateinit var content: CharSequence
    lateinit var commentatorAvatar: String
    lateinit var timeAgo: String

    init {
        if (action.comment != null) {
            this.commentatorAvatar = (if (action.comment.commentatorAvatar.startsWith("https:")) {
                action.comment.commentatorAvatar
            } else {
                "https:${action.comment.commentatorAvatar}"
            })

            this.commentatorHandle = formatCommentatorHandle(
                action.comment.commentatorHandle, action.comment.commentatorRank
            )

            this.title = underlineTitle(action.blogEntry.title)
            this.timeAgo = PrettyTime().format(Date(action.comment.creationTimeSeconds * 1000))
            this.content = convertFromHtml(action.comment.text)
        }
    }

    private fun formatCommentatorHandle(handle: String, rank: String?): CharSequence {
        val colorHandle = colorTextByUserRank(handle, rank, CwApp.app)
        val commentedByString = CwApp.app.getString(R.string.commented_by)
        val handlePosition = commentedByString.indexOf("%1\$s")

        return SpannableStringBuilder(commentedByString)
            .replace(handlePosition, handlePosition + "%1\$s".length, colorHandle)
    }

    private fun underlineTitle(title: String): SpannableString {
        val convertedTitle = convertFromHtml(title)
        val underlinedTitle = SpannableString(convertedTitle)
        underlinedTitle.setSpan(UnderlineSpan(), 0, convertedTitle.length, 0)
        return underlinedTitle
    }

    private fun convertFromHtml(text: String) =
        HtmlCompat.fromHtml(text
            .replace("\n", "<br>")
            .replace("\t", "<tl>")
            .replace("$", ""),
            HtmlCompat.FROM_HTML_MODE_LEGACY).trim()
}