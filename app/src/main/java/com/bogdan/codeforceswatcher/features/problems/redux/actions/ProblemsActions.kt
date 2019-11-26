package com.bogdan.codeforceswatcher.features.problems.redux.actions

import org.rekotlin.Action

class ProblemsActions {

    data class ChangeProblems(val isFavourite: Boolean) : Action
}