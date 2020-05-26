package com.bogdan.codeforceswatcher.util

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.showSoftKeyboard() = post {
    if (this.requestFocus()) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText.hideKeyboard() = onEditorAction(EditorInfo.IME_ACTION_DONE)