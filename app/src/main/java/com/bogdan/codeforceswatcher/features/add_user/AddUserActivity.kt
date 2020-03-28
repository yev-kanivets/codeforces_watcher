package com.bogdan.codeforceswatcher.features.add_user

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.util.Analytics
import io.xorum.codeforceswatcher.features.add_user.redux.actions.AddUserActions
import io.xorum.codeforceswatcher.features.add_user.redux.requests.AddUserRequests
import io.xorum.codeforceswatcher.features.add_user.redux.states.AddUserState
import io.xorum.codeforceswatcher.redux.store
import kotlinx.android.synthetic.main.activity_add_user.*
import tw.geothings.rekotlin.StoreSubscriber

class AddUserActivity : AppCompatActivity(), OnClickListener, StoreSubscriber<AddUserState> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showKeyboard()
        btnAdd.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState ->
                oldState.addUserState == newState.addUserState
            }.select { it.addUserState }
        }
    }

    override fun onPause() {
        hideKeyboard()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        store.dispatch(AddUserActions.ClearAddUserState)
        store.unsubscribe(this)
    }

    private fun showKeyboard() {
        etHandle.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(etHandle.windowToken, 0)
    }

    override fun newState(state: AddUserState) {
        progressBar.visibility = when (state.status) {
            AddUserState.Status.IDLE -> INVISIBLE
            AddUserState.Status.PENDING -> VISIBLE
            AddUserState.Status.DONE -> INVISIBLE
        }
        if (state.status == AddUserState.Status.DONE) finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAdd -> {
                store.dispatch(AddUserRequests.AddUser(etHandle.text.toString()))
                Analytics.logUserAdded()
            }
            else -> {
            }
        }
    }
}
