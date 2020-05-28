package com.bogdan.codeforceswatcher.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.bogdan.codeforceswatcher.R
import kotlinx.android.synthetic.main.input_field.view.*

class InputField(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    enum class Type {
        EMAIL,
        PASSWORD,
        TEXT;

        val textViewInputType: Int
            get() = when (this) {
                EMAIL -> InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                PASSWORD -> InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
                TEXT -> InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }
    }

    sealed class Action(
            val imeActionId: Int
    ) {
        open val onClick: (() -> Unit)? = null

        data class Go(
                override val onClick: (() -> Unit)
        ) : Action(EditorInfo.IME_ACTION_GO)
    }

    init {
        View.inflate(context, R.layout.input_field, this)
    }

    fun configure(
            type: Type = Type.TEXT,
            action: Action? = null
    ) {
        editText.inputType = type.textViewInputType

        if (action != null) {
            editText.imeOptions = action.imeActionId
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == action.imeActionId && action.onClick != null) {
                    action.onClick?.invoke()
                    true
                } else {
                    false
                }
            }
        } else {
            editText.imeOptions = EditorInfo.IME_ACTION_NONE
            editText.setOnEditorActionListener(null)
        }
    }
}
