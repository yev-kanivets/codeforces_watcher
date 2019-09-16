package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.model.Contest
import org.rekotlin.StateType

data class ContestsState(
    val contests: List<Contest> = listOf()
) : StateType
