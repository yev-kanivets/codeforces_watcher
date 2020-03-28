package io.xorum.codeforceswatcher.redux

import tw.geothings.rekotlin.Action

interface ToastAction : Action {
    val message: String?
}
