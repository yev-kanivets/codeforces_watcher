package com.bogdan.codeforceswatcher.redux.states

import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.Contest
import org.rekotlin.StateType

data class UIState(
    val selectedHomeTab: HomeTab = HomeTab.USERS
) : StateType {

    enum class HomeTab {
        USERS,
        CONTESTS,
        ACTIONS,
        PROBLEMS;

        companion object {

            fun fromMenuItemId(menuItemId: Int): HomeTab =
                enumValues<HomeTab>().find { it.menuItemId == menuItemId } ?: USERS
        }

        val menuItemId: Int
            get() = when (this) {
                USERS -> R.id.navUsers
                CONTESTS -> R.id.navContests
                ACTIONS -> R.id.navActions
                PROBLEMS -> R.id.navProblems
            }
    }

}