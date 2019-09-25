package com.bogdan.codeforceswatcher.feature.users.redux.actions

import com.bogdan.codeforceswatcher.feature.users.redux.UsersState
import org.rekotlin.Action

class SortActions {

    data class Sort(
        val sortType: UsersState.SortType
    ) : Action

}