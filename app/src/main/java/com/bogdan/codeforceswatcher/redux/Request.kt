package com.bogdan.codeforceswatcher.redux

import org.rekotlin.Action

abstract class Request : Action {

    abstract suspend fun execute()
}