package com.bogdan.codeforceswatcher.features.users.models

import android.text.SpannableString
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

enum class Update { INCREASE, DECREASE, NULL }

sealed class UserItem {

    class User(user: com.bogdan.codeforceswatcher.features.users.models.User) : UserItem() {

        val id: Long = user.id
        val avatar: String = user.avatar
        var update: Update = Update.NULL
        val handle: SpannableString = colorTextByUserRank(user.handle, user.rank)
        val rating: SpannableString = colorTextByUserRank(user.rating?.toString().orEmpty(), user.rank)
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