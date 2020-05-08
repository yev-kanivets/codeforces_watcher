package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import io.xorum.codeforceswatcher.util.BaseFeedbackController
import io.xorum.codeforceswatcher.util.FeedbackItem


class FeedbackController(private val context: Context): BaseFeedbackController() {
    private val prefs = Prefs.get()

    override var currentShowingItem: Int
        get() = getDefaultPrefs().getInt(KEY_CURRENT_SHOWING_ITEM, 0)
        set(value) {
            val editor = getDefaultPrefs().edit()
            editor.run {
                putInt(KEY_CURRENT_SHOWING_ITEM, value)
                apply()
            }
        }

    override var countScreenOpening: Int
        get() = getDefaultPrefs().getInt(KEY_COUNT_SCREEN_OPENING, 0)
        set(value) {
            val editor = getDefaultPrefs().edit()
            editor.run {
                putInt(KEY_COUNT_SCREEN_OPENING, value)
                apply()
            }
        }

    override var startTimeWhenShown: Long
        get() = getDefaultPrefs().getLong(KEY_START_TIME_SHOWING, 0)
        set(value) {
            val editor = getDefaultPrefs().edit()
            editor.run {
                putLong(KEY_START_TIME_SHOWING, value)
                apply()
            }
        }

    override var isNeededAtAll: Boolean
        get() = getDefaultPrefs().getBoolean(KEY_IS_NEEDED_AT_ALL, true)
        set(value) {
            val editor = getDefaultPrefs().edit()
            editor.run {
                putBoolean(KEY_IS_NEEDED_AT_ALL, value)
                apply()
            }
        }

    override var isLockoutPeriod: Boolean
        get() = getDefaultPrefs().getBoolean(KEY_IS_LOCKOUT_PERIOD, false)
        set(value) {
            val editor = getDefaultPrefs().edit()
            editor.run {
                putBoolean(KEY_IS_LOCKOUT_PERIOD, value)
                apply()
            }
        }

    override fun buildEnjoyingItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedbackItem {
        return FeedbackItem(
                textPositiveButton=context.getString(R.string.yes),
                textNegativeButton=context.getString(R.string.not_really),
                textTitle=context.getString(R.string.rate_us_first_title),
                positiveButtonClick=positiveButtonClick,
                negativeButtonClick=negativeButtonClick,
                neutralButtonClick=neutralButtonClick
        )
    }

    override fun buildEmailItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedbackItem {
        return FeedbackItem(
                textPositiveButton=context.getString(R.string.yes),
                textNegativeButton=context.getString(R.string.no_thanks),
                textTitle=context.getString(R.string.rate_us_second_title),
                positiveButtonClick=positiveButtonClick,
                negativeButtonClick=negativeButtonClick,
                neutralButtonClick=neutralButtonClick
        )
    }

    override fun buildRateItem(
            positiveButtonClick: () -> Unit,
            negativeButtonClick: () -> Unit,
            neutralButtonClick: () -> Unit
    ): FeedbackItem {
        return FeedbackItem(
                textPositiveButton=context.getString(R.string.yes),
                textNegativeButton=context.getString(R.string.no_thanks),
                textTitle=context.getString(R.string.rate_us_third_title),
                positiveButtonClick=positiveButtonClick,
                negativeButtonClick=negativeButtonClick,
                neutralButtonClick=neutralButtonClick
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
        try {
            val uri = Uri.parse(GP_MARKET + context.packageName)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            prefs.appRated()
        } catch (e: Exception) {
            Toast.makeText(
                    context,
                    context.getString(R.string.google_play_not_found),
                    Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    private fun getDefaultPrefs(): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private val feedbackController: FeedbackController = FeedbackController(CwApp.app)

        fun get() = feedbackController

        private const val GP_MARKET = "market://details?id="
    }
}