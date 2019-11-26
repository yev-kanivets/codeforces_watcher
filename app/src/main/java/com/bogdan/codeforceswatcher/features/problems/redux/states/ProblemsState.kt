package com.bogdan.codeforceswatcher.features.problems.redux.states

import com.bogdan.codeforceswatcher.features.problems.models.Problem
import org.rekotlin.StateType

data class ProblemsState(
    val problems: List<Problem> = listOf(),
    val isFavourite: Boolean = false
) : StateType