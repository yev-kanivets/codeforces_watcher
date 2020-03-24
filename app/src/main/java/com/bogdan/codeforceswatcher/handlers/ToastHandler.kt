package com.bogdan.codeforceswatcher.handlers

import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import redux.middlewares.ToastHandler

class AndroidMessageHandler : ToastHandler {

    override fun handle(message: String?) {
        message?.let { showToast(it) }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(CwApp.app, message, Toast.LENGTH_SHORT)
        val textInToast = toast.view.findViewById<TextView>(android.R.id.message)
        textInToast?.gravity = Gravity.CENTER
        toast.show()
    }
}
