package com.bogdan.codeforceswatcher.redux.states

import com.bogdan.codeforceswatcher.features.actions.redux.states.ActionsState
import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.add_user.redux.states.AddUserState
import com.bogdan.codeforceswatcher.features.problems.redux.states.ProblemsState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import tw.geothings.rekotlin.StateType

data class AppState(
        val contests: ContestsState = ContestsState(),
        val users: UsersState = UsersState(),
        val actions: ActionsState = ActionsState(),
        val problems: ProblemsState = ProblemsState(),
        val addUserState: AddUserState = AddUserState(),
        val ui: UIState = UIState()
) : StateType
