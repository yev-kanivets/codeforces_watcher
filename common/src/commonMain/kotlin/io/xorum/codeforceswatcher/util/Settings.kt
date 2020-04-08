package io.xorum.codeforceswatcher.util

import io.xorum.codeforceswatcher.features.contests.models.Platform

lateinit var settings: Settings

interface Settings {

    fun readSpinnerSortPosition(): Int

    fun writeSpinnerSortPosition(spinnerSortPosition: Int)

    fun readProblemsIsFavourite(): Boolean

    fun writeProblemsIsFavourite(isFavourite: Boolean)

    fun readContestsFilters(): Set<Platform>

    fun writeContestsFilters(filters: Set<Platform>)
}
