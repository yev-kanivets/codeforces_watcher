package com.bogdan.codeforceswatcher.redux

import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.add_user.redux.states.AddUserState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import org.rekotlin.StateType

data class AppState(
    val contests: ContestsState = ContestsState(),
    val users: UsersState = UsersState(),
    val addUserState: AddUserState = AddUserState()
) : StateType
