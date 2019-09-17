package com.bogdan.codeforceswatcher.feature.contests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.ContestAdapter
import com.bogdan.codeforceswatcher.feature.contests.redux.ContestsRequests
import com.bogdan.codeforceswatcher.feature.contests.redux.ContestsState
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.fragment_contests.recyclerView
import kotlinx.android.synthetic.main.fragment_contests.swipeToRefresh
import org.rekotlin.StoreSubscriber

class ContestsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    StoreSubscriber<ContestsState> {

    private lateinit var contestAdapter: ContestAdapter

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state -> state.select { it.contests } }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: ContestsState) {
        swipeToRefresh.isRefreshing = (state.status == ContestsState.Status.PENDING)
        contestAdapter.setItems(state.contests.sortedBy(Contest::time))
    }

    override fun onRefresh() {
        store.dispatch(ContestsRequests.FetchContests())
        Analytics.logContestsListRefresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_contests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        store.dispatch(ContestsRequests.FetchContests())
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener(this)

        contestAdapter = ContestAdapter(listOf(), requireContext())
        recyclerView.adapter = contestAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*val liveData = DatabaseClient.contestDao.getUpcomingContests()
        liveData.observe(this, Observer<List<Contest>> { contestList ->
            contestList?.let { contestsList -> contestAdapter.setItems(contestsList.sortedBy(Contest::time)) }
        })*/
    }
}