package com.bogdan.codeforceswatcher.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.network.UserLoader
import kotlinx.android.synthetic.main.fragment_users.recyclerView
import kotlinx.android.synthetic.main.fragment_users.swipeToRefresh

class UsersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var userAdapter: UserAdapter
    private var counterIcon: Int = 0
    private lateinit var spSort: AppCompatSpinner
    private var prefs = Prefs.get()

    override fun onRefresh() {
        UserLoader.loadUsers(shouldDisplayErrors = true) {
            activity?.runOnUiThread {
                swipeToRefresh.isRefreshing = false
            }
        }

        Analytics.logUsersListRefresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        counterIcon = if (prefs.readCounter().isEmpty()) 0 else prefs.readCounter().toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener(this)

        userAdapter = UserAdapter(listOf(), requireContext())
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        spSort = requireActivity().findViewById(R.id.spSort)

        val spinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item,
            resources.getStringArray(R.array.array_sort)
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spSort.adapter = spinnerAdapter
        spSort.setSelection(counterIcon)

        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                counterIcon = position
                updateList(CwApp.app.userDao.getAll())
                prefs.writeCounter(counterIcon)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val liveData = CwApp.app.userDao.getAllLive()
        liveData.observe(this, Observer<List<User>> { userList ->
            userList?.let { usersList -> updateList(usersList) }
        })
    }

    fun updateList(users: List<User>) {
        when (counterIcon) {
            0 -> userAdapter.setItems(users.reversed())
            1 -> userAdapter.setItems(users.sortedByDescending(User::rating))
            2 -> userAdapter.setItems(users.sortedBy(User::rating))
            3 -> userAdapter.setItems(users.sortedByDescending { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            })
            4 -> userAdapter.setItems(users.sortedBy { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            })
        }
    }
}
