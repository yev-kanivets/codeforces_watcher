package io.xorum.codeforceswatcher.redux.middlewares

import io.xorum.codeforceswatcher.coroutines.CustomMainScope
import io.xorum.codeforceswatcher.redux.Request
import kotlinx.coroutines.launch
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

private val scope = CustomMainScope()

val appMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is Request) executeRequest(action)

            next(action)
        }
    }
}

private fun executeRequest(action: Request) = scope.launch { action.execute() }
