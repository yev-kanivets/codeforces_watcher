package com.bogdan.codeforceswatcher.features.actions

import androidx.fragment.app.Fragment
import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import com.bogdan.codeforceswatcher.features.actions.redux.states.ActionsState
import com.bogdan.codeforceswatcher.store
import org.rekotlin.StoreSubscriber

class ActionsFragment : Fragment(), StoreSubscriber<ActionsState> {

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState -> oldState.actions == newState.actions }
                .select { it.actions }
        }

        store.dispatch(ActionsRequests.FetchActions())
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: ActionsState) {
        println(state.status)
        println(state.actions)
    }
}