package io.xorum.codeforceswatcher.redux.middlewares

import io.xorum.codeforceswatcher.redux.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

val appMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            CoroutineScope(Dispatchers.Main).launch {
                (action as? Request)?.execute()
            }
            next(action)
        }
    }
}
