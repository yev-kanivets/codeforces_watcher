package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.MainActivity
import io.xorum.codeforceswatcher.util.BaseFeedbackController
import io.xorum.codeforceswatcher.util.FeedUIModel

class FeedbackController(private val context: Context) : BaseFeedbackController() {
    private val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    override var currentShowingItem: Int
        get() = sharedPreferences.getInt(KEY_CURRENT_SHOWING_ITEM, 0)
        set(value) = editor.putInt(KEY_CURRENT_SHOWING_ITEM, value).apply()

    override var countScreenOpening: Int
        get() = sharedPreferences.getInt(KEY_COUNT_SCREEN_OPENING, 0)
        set(value) = editor.putInt(KEY_COUNT_SCREEN_OPENING, value).apply()

    override var startTimeWhenShown: Long
        get() = sharedPreferences.getLong(KEY_START_TIME_SHOWING, 0)
        set(value) = editor.putLong(KEY_START_TIME_SHOWING, value).apply()

    override var isNeededAtAll: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_NEEDED_AT_ALL, true)
        set(value) = editor.putBoolean(KEY_IS_NEEDED_AT_ALL, value).apply()

    override var isLockoutPeriod: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOCKOUT_PERIOD, false)
        set(value) = editor.putBoolean(KEY_IS_LOCKOUT_PERIOD, value).apply()

    override fun buildEnjoyingItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedUIModel {
        return FeedUIModel(
                textPositiveButton = context.getString(R.string.yes),
                textNegativeButton = context.getString(R.string.not_really),
                textTitle = context.getString(R.string.rate_us_first_title),
                positiveButtonClick = positiveButtonClick,
                negativeButtonClick = negativeButtonClick,
                neutralButtonClick = neutralButtonClick
        )
    }

    override fun buildEmailItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedUIModel {
        return FeedUIModel(
                textPositiveButton = context.getString(R.string.yes),
                textNegativeButton = context.getString(R.string.no_thanks),
                textTitle = context.getString(R.string.rate_us_second_title),
                positiveButtonClick = positiveButtonClick,
                negativeButtonClick = negativeButtonClick,
                neutralButtonClick = neutralButtonClick
        )
    }

    override fun buildRateItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedUIModel {
        return FeedUIModel(
                textPositiveButton = context.getString(R.string.yes),
                textNegativeButton = context.getString(R.string.no_thanks),
                textTitle = context.getString(R.string.rate_us_third_title),
                positiveButtonClick = positiveButtonClick,
                negativeButtonClick = negativeButtonClick,
                neutralButtonClick = neutralButtonClick
        )
    }

    override fun showEmailApp() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:support@xorum.io?subject=Feedback about Codeforces WatchR App")

        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) { //TODO: Handle case where no email app is available
        }
    }

    override fun showAppStore() {
        val appPackageName = context.packageName
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (activityNotFoundException: ActivityNotFoundException) {
            context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
            )
        }
    }

    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var feedbackController: FeedbackController

        fun get() = feedbackController
    }
}