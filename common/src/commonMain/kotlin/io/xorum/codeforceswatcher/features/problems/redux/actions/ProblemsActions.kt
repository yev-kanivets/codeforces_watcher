package io.xorum.codeforceswatcher.features.problems.redux.actions

import tw.geothings.rekotlin.Action

class ProblemsActions {

    data class ChangeTypeProblems(val isFavourite: Boolean) : Action
}
