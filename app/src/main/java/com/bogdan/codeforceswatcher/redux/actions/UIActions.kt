package com.bogdan.codeforceswatcher.redux.actions

import com.bogdan.codeforceswatcher.redux.states.UIState
import org.rekotlin.Action

class UIActions {

    data class SelectHomeTab(val homeTab: UIState.HomeTab) : Action
}