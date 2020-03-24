package redux.middlewares

import redux.ToastAction
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

interface ToastHandler {

    fun handle(message: String?)
}

var toastHandler: ToastHandler? = null

val toastMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is ToastAction) {
                toastHandler?.handle(action.message)
            }
            next(action)
        }
    }
}
