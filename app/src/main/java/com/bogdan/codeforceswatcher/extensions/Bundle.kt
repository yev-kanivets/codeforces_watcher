package com.bogdan.codeforceswatcher.extensions

import android.os.Bundle
import java.io.Serializable

var Bundle.onConfirm: ((String) -> Unit)?
    get() = getSerializable("order_feature") as? ((String) -> Unit)
    set(value) = putSerializable("order_feature", value as Serializable)

var Bundle.taskTitleResId: Int?
    get() = getInt("task_title")
    set(value) {
        if (value != null) {
            putInt("task_title", value)
        }
    }

var Bundle.actionButtonTitleResId: Int?
    get() = getInt("action_button_title")
    set(value) {
        if (value != null) {
            putInt("action_button_title", value)
        }
    }