package com.bogdan.codeforceswatcher.features.users

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

enum class Update { INCREASE, DECREASE, NULL }

sealed class UserItem {

    class User(user: io.xorum.codeforceswatcher.features.users.models.User) : UserItem() {

        val id: Long = user.id
        val avatar: String = user.avatar
        var update: Update = Update.NULL
        val handle: SpannableString = colorTextByUserRank(user.handle, user.rank)
        val rating: SpannableString = colorTextByUserRank(user.rating?.toString()
                ?: user.ratingChanges.lastOrNull()?.newRating?.toString().orEmpty(), user.rank)
        var lastRatingUpdate: String = ""
        var dateOfLastRatingUpdate: String = CwApp.app.getString(R.string.no_rating_update)

        init {
            user.ratingChanges.lastOrNull()?.let { ratingChange ->
                dateOfLastRatingUpdate = CwApp.app.getString(
                        R.string.last_rating_update,
                        getDateTime(ratingChange.ratingUpdateTimeSeconds)
                )
                val difference = ratingChange.newRating - ratingChange.oldRating
                update = if (difference >= 0) Update.INCREASE else Update.DECREASE
                lastRatingUpdate = abs(difference).toString()
            }
        }

        private fun getDateTime(seconds: Long): String {
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            return dateFormat.format(Date(seconds * 1000)).toString()
        }
    }

    object Stub : UserItem()
}

fun colorTextByUserRank(text: String, rank: String?): SpannableString {
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
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(CwApp.app, color))
        colorText.setSpan(foregroundColorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        colorText
    }
}
