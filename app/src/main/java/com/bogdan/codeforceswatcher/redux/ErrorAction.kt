package com.bogdan.codeforceswatcher.redux

import org.rekotlin.Action

interface ErrorAction : Action {
    val message: String?
}