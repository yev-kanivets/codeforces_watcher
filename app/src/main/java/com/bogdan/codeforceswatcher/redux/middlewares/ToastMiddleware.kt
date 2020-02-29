package com.bogdan.codeforceswatcher.redux.middlewares

import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.redux.actions.ToastAction
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

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
    val toast = Toast.makeText(CwApp.app, message, Toast.LENGTH_SHORT)
    val textInToast = toast.view.findViewById<TextView>(android.R.id.message)
    textInToast?.gravity = Gravity.CENTER
    toast.show()
}
