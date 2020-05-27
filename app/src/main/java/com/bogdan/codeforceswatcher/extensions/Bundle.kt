package com.bogdan.codeforceswatcher.extensions

import android.os.Bundle
import java.io.Serializable

var Bundle.onConfirm: ((String) -> Unit)?
    get() = getSerializable("on_confirm") as? ((String) -> Unit)
    set(value) = putSerializable("on_confirm", value as Serializable)

var Bundle.taskTitleResId: Int?
    get() = getInt("task_title_id")
    set(value) {
        if (value != null) {
            putInt("task_title_id", value)
        }
    }

var Bundle.actionButtonTitleResId: Int?
    get() = getInt("action_button_title_id")
    set(value) {
        if (value != null) {
            putInt("action_button_title_id", value)
        }
    }