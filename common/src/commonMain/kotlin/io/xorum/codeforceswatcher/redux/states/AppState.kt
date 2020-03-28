package io.xorum.codeforceswatcher.redux.states

import io.xorum.codeforceswatcher.features.actions.redux.states.ActionsState
import io.xorum.codeforceswatcher.features.add_user.redux.states.AddUserState
import io.xorum.codeforceswatcher.features.problems.redux.states.ProblemsState
import io.xorum.codeforceswatcher.features.contests.redux.states.ContestsState
import io.xorum.codeforceswatcher.features.users.redux.states.UsersState
import tw.geothings.rekotlin.StateType

data class AppState(
        val contests: ContestsState = ContestsState(),
        val users: UsersState = UsersState(),
        val actions: ActionsState = ActionsState(),
        val problems: ProblemsState = ProblemsState(),
        val addUserState: AddUserState = AddUserState()
) : StateType
