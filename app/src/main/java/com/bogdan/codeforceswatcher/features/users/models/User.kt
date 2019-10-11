package com.bogdan.codeforceswatcher.features.users.models

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bogdan.codeforceswatcher.R

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val avatar: String,
    val rank: String?,
    val handle: String,
    val rating: Int?,
    val maxRating: Int?,
    val firstName: String?,
    val lastName: String?,
    var ratingChanges: List<RatingChange>
)

fun getColorTextByUserRank(text: String, rank: String?, context: Context): SpannableString {
    val color = when (rank) {
        null -> R.color.black
        "newbie" -> R.color.grey
        "pupil" -> R.color.green
        "specialist" -> R.color.blue_green
        "expert" -> R.color.blue
        "candidate master" -> R.color.purple
        "master" -> R.color.orange
        "international master" -> R.color.orange
        "grandmaster" -> R.color.red
        "international grandmaster" -> R.color.red
        else -> R.color.grey
    }

    return if (rank == "legendary grandmaster") {
        val colorText = "<font color=black>${text[0]}</font><font color=red>${
        text.subSequence(1, text.length)
        }</font>"
        SpannableString(HtmlCompat.fromHtml(colorText, HtmlCompat.FROM_HTML_MODE_LEGACY))
    } else {
        val colorText = SpannableString(text)
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(context, color))
        colorText.setSpan(foregroundColorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        colorText
    }
}
