package com.bogdan.codeforceswatcher.feature.contests.redux

import com.bogdan.codeforceswatcher.model.Contest
import org.rekotlin.StateType

data class ContestsState(
    val status: Status = Status.IDLE,
    val contests: List<Contest> = listOf()
) : StateType {

    enum class Status { IDLE, PENDING }
}
