package com.bogdan.codeforceswatcher.redux

import org.rekotlin.Action

interface ToastAction : Action {
    val message: String?
}