package com.bogdan.codeforceswatcher.components

import android.os.Bundle
import com.bogdan.codeforceswatcher.R
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.extensions.actionButtonTitleResId
import com.bogdan.codeforceswatcher.extensions.onConfirm
import com.bogdan.codeforceswatcher.extensions.taskTitleResId
import com.bogdan.codeforceswatcher.util.showSoftKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.xorum.codeforceswatcher.features.users.redux.states.UsersState
import io.xorum.codeforceswatcher.redux.store
import kotlinx.android.synthetic.main.card_with_edit_text.*
import kotlinx.android.synthetic.main.input_field.*
import tw.geothings.rekotlin.StoreSubscriber

class CardWithEditText : BottomSheetDialogFragment(), StoreSubscriber<UsersState> {

    companion object {

        fun newInstance(
                actionButtonTitleResId: Int,
                taskTitleResId: Int,
                onConfirm: ((String) -> Unit)? = null
        ) = CardWithEditText().apply {
            arguments = Bundle().apply {
                this.actionButtonTitleResId = actionButtonTitleResId
                this.taskTitleResId = taskTitleResId
                this.onConfirm = onConfirm
            }
        }
    }

    private val actionButtonTitleResId: Int?
            get() = arguments?.actionButtonTitleResId
    private val taskTitleResId: Int?
            get() = arguments?.taskTitleResId
    private val onConfirm: ((String) -> Unit)?
            get() = arguments?.onConfirm

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.card_with_edit_text, container, false)
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState ->
                oldState.users == newState.users
            }.select { it.users }
        }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UsersState) {
        progressBar.visibility = when (state.addUserStatus) {
            UsersState.Status.IDLE -> INVISIBLE
            UsersState.Status.PENDING -> VISIBLE
            UsersState.Status.DONE -> INVISIBLE
        }

        when (state.addUserStatus) {
            UsersState.Status.DONE -> {
                editText.text.clear()
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText.addTextChangedListener(textEditorWatcher)

        actionButtonTitleResId?.let {
            actionButton.text = CwApp.app.getString(it)
        }

        actionButton.setOnClickListener {
            onConfirm?.invoke(editText.text?.toString() ?: "")
        }

        taskTitleResId?.let {
            task.text = CwApp.app.getString(it)
        }
        inputField.configure(
                action = InputField.Action.Go {
                    onConfirm?.invoke(editText.text?.toString() ?: "")
                }
        )
    }

    override fun onResume() {
        super.onResume()
        editText.showSoftKeyboard()
    }

    private val textEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            editText.setSelection(s.length)
        }
    }
}
