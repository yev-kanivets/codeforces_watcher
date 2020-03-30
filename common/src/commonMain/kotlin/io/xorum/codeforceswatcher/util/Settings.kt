package io.xorum.codeforceswatcher.util

lateinit var settings: Settings

interface Settings {

    fun readSpinnerSortPosition(): Int

    fun writeSpinnerSortPosition(spinnerSortPosition: Int)

    fun readProblemsIsFavourite(): Boolean

    fun writeProblemsIsFavourite(isFavourite: Boolean)
}
