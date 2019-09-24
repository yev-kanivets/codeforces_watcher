package com.bogdan.codeforceswatcher.redux.middlewares

import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.redux.ErrorAction
import org.rekotlin.Middleware
import org.rekotlin.StateType

val errorMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is ErrorAction) {
                action.message?.let {
                    showError(it)
                }
            }
            next(action)
        }
    }
}

private fun showError(message: String) {
    Toast.makeText(CwApp.app, message, Toast.LENGTH_SHORT).show()
}