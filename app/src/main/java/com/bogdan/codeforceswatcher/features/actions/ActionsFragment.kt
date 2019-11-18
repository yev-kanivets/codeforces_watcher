package com.bogdan.codeforceswatcher.features.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.models.ActionItem
import com.bogdan.codeforceswatcher.features.actions.models.CFAction
import com.bogdan.codeforceswatcher.features.actions.redux.requests.ActionsRequests
import com.bogdan.codeforceswatcher.features.actions.redux.states.ActionsState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.Refresh
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
        swipeRefreshLayout.isRefreshing = (state.status == ActionsState.Status.PENDING)
        actionsAdapter.setItems(buildActionItems(state.actions))
    }

    private fun buildActionItems(actions: List<CFAction>) =
        actions.map {
            if (it.comment != null) {
                ActionItem.CommentItem(it)
            } else {
                ActionItem.BlogEntryItem(it)
            }
        }

    override fun onRefresh() {
        store.dispatch(ActionsRequests.FetchActions(true))
        Analytics.logRefreshingData(Refresh.ACTIONS)
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
        swipeRefreshLayout.setOnRefreshListener(this)
        actionsAdapter = ActionsAdapter(requireContext()) { actionIndex ->
            startActivity(ActionActivity.newIntent(requireContext(), store.state.actions.actions[actionIndex]))
        }
        recyclerView.adapter = actionsAdapter
    }
}