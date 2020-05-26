package com.bogdan.codeforceswatcher.components

import android.app.Dialog
import android.os.Bundle
import com.bogdan.codeforceswatcher.R
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.extensions.actionButtonTitleResId
import com.bogdan.codeforceswatcher.extensions.onConfirm
import com.bogdan.codeforceswatcher.extensions.taskTitleResId
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.card_with_edit_text.*
import kotlinx.android.synthetic.main.input_field.*

class CardWithEditText : BottomSheetDialogFragment() {

    interface OnDismissListener {

        fun onDismiss()
    }

    private val actionButtonTitleResId: Int? = arguments?.actionButtonTitleResId
    private val taskTitleResId: Int? = arguments?.taskTitleResId
    private val onConfirm: ((String) -> Unit)? = arguments?.onConfirm

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.card_with_edit_text, container, false)
    }

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            with(BottomSheetBehavior.from(bottomSheet)) {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
                isHideable = false
                isFitToContents = true
                isCancelable = false
            }
        }
        return dialog
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText.addTextChangedListener(textEditorWatcher)

        actionButtonTitleResId?.let {
            actionButton.text = CwApp.app.getString(actionButtonTitleResId)
        }

        actionButton.setOnClickListener {
            onConfirm?.invoke(editText.text?.toString() ?: "")
        }

        taskTitleResId?.let {
            task.text = CwApp.app.getString(taskTitleResId)
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

    fun dismissAndNotifyListeners() = activity?.let { activity ->
        dismissAllowingStateLoss()
        if (activity is OnDismissListener) activity.onDismiss()
    }
}
