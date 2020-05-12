package io.xorum.codeforceswatcher.util

abstract class BaseFeedbackController {

    abstract var countScreenOpening: Int

    abstract var startTimeWhenShown: Long

    abstract var isNeededAtAll: Boolean

    abstract var isLockoutPeriod: Boolean

    abstract var currentShowingItem: Int

    var feedbackData: FeedbackData
        get() = when(currentShowingItem) {
            0 -> buildEnjoyingItem()
            1 -> { buildEmailItem().also { isNeededAtAll = false } }
            2 -> buildRateItem()
            else -> {
                throw IllegalStateException()
            }
        }
        set(v) {}

    abstract fun buildEnjoyingItem(
            positiveButtonClick: () -> Unit = { currentShowingItem = 2 },
            negativeButtonClick: () -> Unit = { currentShowingItem = 1 },
            neutralButtonClick: () -> Unit = { turnOnLockoutPeriod() }
    ): FeedbackData

    abstract fun buildEmailItem(
            positiveButtonClick: () -> Unit = { showEmailApp() },
            negativeButtonClick: () -> Unit = { turnOnLockoutPeriod() },
            neutralButtonClick: () -> Unit = { turnOnLockoutPeriod() }
    ): FeedbackData

    abstract fun buildRateItem(
            positiveButtonClick: () -> Unit = { showAppStore().also { isNeededAtAll = false } },
            negativeButtonClick: () -> Unit = { turnOnLockoutPeriod() },
            neutralButtonClick: () -> Unit = { turnOnLockoutPeriod() }
    ): FeedbackData

    abstract fun showEmailApp()

    abstract fun showAppStore()

    private fun turnOnLockoutPeriod() {
        isLockoutPeriod = true
        currentShowingItem = 0

        startTimeWhenShown = currentTimeMillis()
        countScreenOpening = 0
    }

    fun shouldShowFeedbackCell(): Boolean {
        if (startTimeWhenShown == 0L) { startTimeWhenShown = currentTimeMillis() }

        val timeInHours = calculateTimeInHours(startTimeWhenShown, currentTimeMillis())
        updateLockoutPeriod(timeInHours)

        return isNeededAtAll && !isLockoutPeriod && (countScreenOpening >= 10 && timeInHours >= 72)
    }

    private fun updateLockoutPeriod(timeInHours: Int) {
        if (timeInHours >= 168 && isLockoutPeriod) { isLockoutPeriod = false }
    }

    private fun calculateTimeInHours(startTimeInMillis: Long, endTimeInMillis: Long): Int {
        return fromMillisToHours(endTimeInMillis - startTimeInMillis)
    }

    private fun fromMillisToHours(timeInMillis: Long): Int {
        return (timeInMillis / 1000 / 60 / 60).toInt()
    }

    abstract fun currentTimeMillis(): Long

    fun updateCountOpeningScreen() {
        countScreenOpening++
    }

    companion object {
        const val KEY_CURRENT_SHOWING_ITEM = "key_current_showing_item"

        const val KEY_COUNT_SCREEN_OPENING = "key_count_app_opening"

        const val KEY_START_TIME_SHOWING = "key_first_time_shown"

        const val KEY_IS_NEEDED_AT_ALL = "key_is_needed_to_show"

        const val KEY_IS_LOCKOUT_PERIOD = "key_is_lockout_period"
    }
}

data class FeedbackData(
        val textPositiveButton: String,
        val textNegativeButton: String,
        val textTitle: String,
        val positiveButtonClick: () -> Unit,
        val negativeButtonClick: () -> Unit,
        val neutralButtonClick: () -> Unit
)
