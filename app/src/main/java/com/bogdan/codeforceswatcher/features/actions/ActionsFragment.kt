package com.bogdan.codeforceswatcher.features.actions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.ActionItem
import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import com.bogdan.codeforceswatcher.features.actions.redux.states.ActionsState
import com.bogdan.codeforceswatcher.store
import kotlinx.android.synthetic.main.fragment_users.*
import org.rekotlin.StoreSubscriber

class ActionsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    StoreSubscriber<ActionsState> {

    private lateinit var actionsAdapter: ActionsAdapter

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState -> oldState.actions == newState.actions }
                .select { it.actions }
        }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: ActionsState) {
        swipeToRefresh.isRefreshing = (state.status == ActionsState.Status.PENDING)
        actionsAdapter.setItems(state.actions.map { ActionItem(it) })
    }

    override fun onRefresh() {
        store.dispatch(ActionsRequests.FetchActions(true))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_actions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener(this)
        actionsAdapter = ActionsAdapter(requireContext()) {
            startActivity(Intent(context, ActionActivity::class.java))
        }
        recyclerView.adapter = actionsAdapter
    }
}