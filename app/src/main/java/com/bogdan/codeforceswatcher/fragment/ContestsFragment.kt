package com.bogdan.codeforceswatcher.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.ContestAdapter
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.model.ContestResponse
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.fragment_contests.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var contestAdapter: ContestAdapter

    override fun onRefresh() {
        updateContestList()
        Analytics.logContestsListRefresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener(this)
        updateContestList(false)

        contestAdapter = ContestAdapter(listOf(), requireContext())
        recyclerView.adapter = contestAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val liveData = CwApp.app.contestDao.getUpcomingContests()
        liveData.observe(this, Observer<List<Contest>> { contestList ->
            contestList?.let { contestsList -> contestAdapter.setItems(contestsList.sortedBy(Contest::time)) }
        })
    }

    private fun updateContestList(shouldDisplayError: Boolean = true) {
        val contestCall = CwApp.app.codeforcesApi.getContests()
        contestCall.enqueue(object : Callback<ContestResponse> {
            override fun onResponse(call: Call<ContestResponse>, response: Response<ContestResponse>) {
                if (response.body() != null) {
                    val contestList = response.body()?.result
                    if (contestList != null) {
                        CwApp.app.contestDao.deleteAll(contestList)
                        CwApp.app.contestDao.insert(contestList)
                    }
                }
                if (activity != null)
                    swipeToRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
                if (activity != null)
                    swipeToRefresh.isRefreshing = false
                if (shouldDisplayError)
                    Toast.makeText(CwApp.app, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }
        })
    }

}