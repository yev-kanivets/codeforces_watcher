package com.bogdan.codeforceswatcher.redux.middlewares

import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.redux.ToastAction
import org.rekotlin.Middleware
import org.rekotlin.StateType

val toastMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is ToastAction) {
                action.message?.let { showToast(it) }
            }
            next(action)
        }
    }
}

private fun showToast(message: String) {
    Toast.makeText(CwApp.app, message, Toast.LENGTH_SHORT).show()
}