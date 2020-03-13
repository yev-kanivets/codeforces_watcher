package com.bogdan.codeforceswatcher.redux.actions

import tw.geothings.rekotlin.Action

interface ToastAction : Action {
    val message: String?
}
