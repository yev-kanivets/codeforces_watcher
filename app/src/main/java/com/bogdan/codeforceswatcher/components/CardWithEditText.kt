package com.bogdan.codeforceswatcher.components

import com.bogdan.codeforceswatcher.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.card_with_edit_text.view.*
import kotlinx.android.synthetic.main.input_field.view.*

class CardWithEditText(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.card_with_edit_text, this)
    }

    private var onConfirm: ((String) -> Unit)? = null
    private var shouldConfirm: Boolean = false

    fun configure(
            actionButtonTitleResId: Int? = null,
            taskTitleResId: Int? = null,
            shouldConfirm: Boolean = true,
            onConfirm: ((String) -> Unit)? = null
    ) {
        this.onConfirm = onConfirm
        this.shouldConfirm = shouldConfirm
        editText.addTextChangedListener(textEditorWatcher)

        actionButtonTitleResId?.let {
            actionButton.text = context.getString(actionButtonTitleResId)
        }
        if (shouldConfirm) {
            actionButton.setOnClickListener {
                onConfirm?.invoke(editText.text?.toString() ?: "")
            }
        } else {
            actionButton.visibility = View.GONE
        }

        taskTitleResId?.let {
            task.text = context.getString(taskTitleResId)
        }
        inputField.configure(
                action = InputField.Action.Go {
                    onConfirm?.invoke(editText.text?.toString() ?: "")
                }
        )
    }

    private val textEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            editText.setSelection(s.length)
        }
    }
}
