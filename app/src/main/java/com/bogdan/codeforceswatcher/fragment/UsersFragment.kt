package com.bogdan.codeforceswatcher.fragment

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.activity.AddUserActivity
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var userAdapter: UserAdapter
    private var counterIcon: Int = 0
    private lateinit var spSort: AppCompatSpinner
    private var prefs = Prefs.get()
    private var itemsOnScreen = 0

    override fun onRefresh() {
        UserLoader.loadUsers(shouldDisplayErrors = true) {
            requireActivity().runOnUiThread {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        counterIcon = if (prefs.readCounter().isEmpty()) 0 else prefs.readCounter().toInt()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        fab.setOnClickListener { startActivity(Intent(requireContext(), AddUserActivity::class.java)) }
        swipeToRefresh.setOnRefreshListener(this)

        userAdapter = UserAdapter(listOf(), requireContext())
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })

        spSort = requireActivity().findViewById(R.id.spSort)

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item,
                resources.getStringArray(R.array.array_sort))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spSort.adapter = spinnerAdapter
        spSort.setSelection(counterIcon)

        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                counterIcon = position
                updateList(CwApp.app.userDao.getAll())
                prefs.writeCounter(counterIcon)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val liveData = CwApp.app.userDao.getAllLive()
        liveData.observe(this, Observer<List<User>> { userList ->
            userList?.let { usersList ->
                updateList(usersList)
                val firstVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition()
                val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                if (lastVisiblePosition - firstVisiblePosition > itemsOnScreen) {
                    itemsOnScreen = lastVisiblePosition - firstVisiblePosition
                } else {
                    fab.show()
                }
            }
        })
    }

    fun updateList(users: List<User>) {
        when (counterIcon) {
            0 -> userAdapter.setItems(users.reversed())
            1 -> userAdapter.setItems(users.sortedByDescending(User::rating))
            2 -> userAdapter.setItems(users.sortedBy(User::rating))
            3 -> userAdapter.setItems(users.sortedByDescending { user -> user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds })
            4 -> userAdapter.setItems(users.sortedBy { user -> user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds })
        }
    }

}