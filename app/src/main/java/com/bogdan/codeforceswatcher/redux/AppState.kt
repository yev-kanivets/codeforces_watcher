package com.bogdan.codeforceswatcher.redux

import com.bogdan.codeforceswatcher.feature.contests.redux.ContestsState
import org.rekotlin.StateType

data class AppState(
    val contests: ContestsState = ContestsState()
) : StateType
