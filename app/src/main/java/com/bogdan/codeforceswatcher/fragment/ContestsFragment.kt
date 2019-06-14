package com.bogdan.codeforceswatcher.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.ContestAdapter
import com.bogdan.codeforceswatcher.model.Contest
import com.bogdan.codeforceswatcher.model.ContestResponse
import kotlinx.android.synthetic.main.fragment_contests.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContestsFragment : android.support.v4.app.Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var contestAdapter: ContestAdapter

    override fun onRefresh() = updateContestList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener(this)
        updateContestList()

        contestAdapter = ContestAdapter(listOf(), requireContext())
        recyclerView.adapter = contestAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val liveData = CwApp.app.contestDao.getUpcomingContests()
        liveData.observe(this, Observer<List<Contest>> { contestList ->
            contestList?.let { contestsList -> contestAdapter.setItems(contestsList.sortedBy(Contest::time)) }
        })
    }

    private fun updateContestList() {
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
                swipeToRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<ContestResponse>, t: Throwable) {
                swipeToRefresh.isRefreshing = false
            }
        })
    }

}