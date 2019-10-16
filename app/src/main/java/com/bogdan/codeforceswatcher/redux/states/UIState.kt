package com.bogdan.codeforceswatcher.redux.states

import com.bogdan.codeforceswatcher.R
import org.rekotlin.StateType

data class UIState(
    val selectedHomeTab: HomeTab = HomeTab.USERS
) : StateType {

    enum class HomeTab(val titleId: Int, val menuItemId: Int) {

        USERS(R.string.empty, R.id.navUsers),
        CONTESTS(R.string.contests, R.id.navContests),
        ACTIONS(R.string.actions, R.id.navActions),
        PROBLEMS(R.string.problems, R.id.navProblems);

        companion object {

            fun fromMenuItemId(menuItemId: Int): HomeTab =
                enumValues<HomeTab>().find { it.menuItemId == menuItemId } ?: USERS
        }
    }

}