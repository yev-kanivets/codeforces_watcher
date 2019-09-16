package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.model.Contest
import org.rekotlin.Action

class ContestsActions {

    data class ContestsLoaded(
        val contests: List<Contest>
    ) : Action
}
