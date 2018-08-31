package com.bogdan.codeforceswatcher.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.activity.AddUserActivity
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : android.support.v4.app.Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private lateinit var userAdapter: UserAdapter
    private var counterIcon: Int = 0

    override fun onRefresh() {
        UserLoader.loadUsers(CwApp.app.userDao.getAll()) { activity?.runOnUiThread { swiperefresh.isRefreshing = false } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val prefs = Prefs(it)
            counterIcon = if (prefs.readCounter().isEmpty()) 0 else prefs.readCounter().toInt()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        activity?.let {
            val fab = it.findViewById<FloatingActionButton>(R.id.fab)
            val spSort = it.findViewById<AppCompatSpinner>(R.id.spSort)
            fab.visibility = View.VISIBLE
            spSort.visibility = View.VISIBLE
            fab.setOnClickListener(this)
            swiperefresh.setOnRefreshListener(this)

            val spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.array_sort))
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spSort.adapter = spinnerAdapter

            spSort.setSelection(counterIcon)

            spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    counterIcon = position
                    updateList(CwApp.app.userDao.getAll())
                    val prefs = Prefs(it)
                    prefs.writeCounter(counterIcon)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

            userAdapter = UserAdapter(listOf(), it)
            rvMain.adapter = userAdapter
            rvMain.layoutManager = LinearLayoutManager(it)
            rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && fab?.visibility == View.VISIBLE) {
                        fab.hide()
                    } else if (dy < 0 && fab?.visibility != View.VISIBLE) {
                        fab?.show()
                    }
                }
            })

            val liveData = CwApp.app.userDao.getAllLive()
            liveData.observe(this, Observer<List<User>> { userList ->
                userList?.let { usersList -> updateList(usersList) }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> startActivity(Intent(activity, AddUserActivity::class.java))
            else -> {
            }
        }
    }

    private fun updateList(users: List<User>) {
        when (counterIcon) {
            0 -> userAdapter.setItems(users.reversed())
            1 -> userAdapter.setItems(users.sortedByDescending(User::rating))
            2 -> userAdapter.setItems(users.sortedBy(User::rating))
            3 -> userAdapter.setItems(users.sortedByDescending { user -> user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds })
            4 -> userAdapter.setItems(users.sortedBy { user -> user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds })
        }
    }

}