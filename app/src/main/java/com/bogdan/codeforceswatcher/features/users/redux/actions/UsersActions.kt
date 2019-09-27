package com.bogdan.codeforceswatcher.features.users.redux.actions

import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import org.rekotlin.Action

class UsersActions {

    data class Sort(
        val sortType: UsersState.SortType
    ) : Action

}