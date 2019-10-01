package com.bogdan.codeforceswatcher.redux.actions

import org.rekotlin.Action

interface ToastAction : Action {
    val message: String?
}