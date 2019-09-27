package com.bogdan.codeforceswatcher.features.add_user

import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.add_user.redux.actions.AddUserActions
import com.bogdan.codeforceswatcher.features.add_user.redux.requests.AddUserRequests
import com.bogdan.codeforceswatcher.features.add_user.redux.states.AddUserState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.activity_add_user.*
import org.rekotlin.StoreSubscriber

class AddUserActivity : AppCompatActivity(), OnClickListener, StoreSubscriber<AddUserState> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnAdd.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state -> state.select { it.addUserState } }
    }

    override fun onStop() {
        super.onStop()

        store.dispatch(AddUserActions.ClearAddUserState)
        store.unsubscribe(this)
    }

    override fun newState(state: AddUserState) {
        progressBar.visibility = when (state.status) {
            AddUserState.Status.IDLE -> INVISIBLE
            AddUserState.Status.PENDING -> VISIBLE
            AddUserState.Status.DONE -> INVISIBLE
        }
        if (state.status == AddUserState.Status.DONE) {
            finish()
        }
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
