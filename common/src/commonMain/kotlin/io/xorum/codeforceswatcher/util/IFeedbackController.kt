package io.xorum.codeforceswatcher.util

abstract class IFeedbackController {
    abstract var countScreenOpening: Int

    abstract var startTimeWhenShown: Long

    abstract var isNeededAtAll: Boolean

    abstract var isLockoutPeriod: Boolean

    abstract var currentShowingItem: Int

    var feedbackItem: FeedbackItem
        get() = when(currentShowingItem) {
            0 -> buildEnjoyingItem()
            1 -> { isNeededAtAll = false; buildEmailItem() }
            2 -> buildRateItem()
            else -> {
                throw IllegalStateException()
            }
        }
        set(v) {}

    abstract fun buildEmailItem(): FeedbackItem

    abstract fun buildEnjoyingItem(): FeedbackItem

    abstract fun buildRateItem(): FeedbackItem

    abstract fun showEmailApp()

    abstract fun showAppStore()

    fun turnOnLockoutPeriod() {
        isLockoutPeriod = true
        currentShowingItem = 0

        startTimeWhenShown = currentTimeMillis()
        countScreenOpening = 0
    }

    fun shouldShowFeedbackCell(): Boolean {
        if (startTimeWhenShown == 0L) { startTimeWhenShown = currentTimeMillis() }

        val timeInHours = calculateTimeInHours(startTimeWhenShown, currentTimeMillis())
        updateLockoutPeriod(timeInHours)

        return isNeededAtAll && !isLockoutPeriod && (countScreenOpening >= 10 && timeInHours >= 72 || countScreenOpening >= 1)
    }

    fun updateLockoutPeriod(timeInHours: Int) {
        if (timeInHours >= 168 && isLockoutPeriod) { isLockoutPeriod = false }
    }

    fun calculateTimeInHours(startTimeInMillis: Long, endTimeInMillis: Long): Int {
        return fromMillisToHours(endTimeInMillis - startTimeInMillis)
    }

    fun fromMillisToHours(timeInMillis: Long): Int {
        return (timeInMillis / 1000 / 60 / 60).toInt()
    }

    abstract fun currentTimeMillis(): Long

    fun updateCountOpeningScreen() {
        countScreenOpening++
    }
}

data class FeedbackItem(
        val textPositiveButton: String,
        val textNegativeButton: String,
        val textTitle: String,
        val positiveButtonClick: () -> Unit,
        val negativeButtonClick: () -> Unit,
        val neutralButtonClick: () -> Unit
)
