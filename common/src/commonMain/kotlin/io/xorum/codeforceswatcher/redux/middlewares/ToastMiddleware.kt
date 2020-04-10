package io.xorum.codeforceswatcher.redux.middlewares

import io.xorum.codeforceswatcher.redux.Message
import io.xorum.codeforceswatcher.redux.ToastAction
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

interface ToastHandler {

    fun handle(message: Message)
}

val toastHandlers = mutableListOf<ToastHandler>()

val toastMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is ToastAction) {
                toastHandlers.forEach { it.handle(action.message) }
            }
            next(action)
        }
    }
}
