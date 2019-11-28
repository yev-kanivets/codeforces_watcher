package com.bogdan.codeforceswatcher.features.problems.redux.actions

import org.rekotlin.Action

class ProblemsActions {

    data class ChangeTypeProblems(val isFavourite: Boolean) : Action
}