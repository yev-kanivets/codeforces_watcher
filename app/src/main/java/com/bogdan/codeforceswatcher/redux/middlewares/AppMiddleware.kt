package com.bogdan.codeforceswatcher.redux.middlewares

import com.bogdan.codeforceswatcher.redux.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rekotlin.Middleware
import org.rekotlin.StateType

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