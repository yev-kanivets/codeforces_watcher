package com.bogdan.codeforceswatcher.redux.states

import com.bogdan.codeforceswatcher.R
import org.rekotlin.StateType

data class UIState(
    val selectedHomeTab: HomeTab = HomeTab.USERS
) : StateType {

    enum class HomeTab {
        USERS,
        CONTESTS;

        companion object {

            fun fromMenuItemId(menuItemId: Int): HomeTab =
                enumValues<HomeTab>().find { it.menuItemId == menuItemId } ?: USERS
        }

        val menuItemId: Int
            get() = when (this) {
                USERS -> R.id.navUsers
                CONTESTS -> R.id.navContests
            }
    }

}