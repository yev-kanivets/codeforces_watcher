package com.bogdan.codeforceswatcher.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.receiver.RatingUpdateReceiver
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private lateinit var userAdapter: UserAdapter
    private var counterIcon: Int = 0
    private val prefs = Prefs(this)
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        counterIcon = if (prefs.readCounter().isEmpty()) 0 else prefs.readCounter().toInt()

        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
        }

        initViews()
    }

    private fun initViews() {
        fab.setOnClickListener(this)
        swiperefresh.setOnRefreshListener(this)

        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
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

        userAdapter = UserAdapter(listOf(), this)

        rvMain.adapter = userAdapter
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })

        val liveData = CwApp.app.userDao.getAllLive()
        liveData.observe(this, Observer<List<User>> { userList ->
            userList?.let { updateList(it) }
        })
    }

    private fun startAlarm() {
        val intent = Intent(applicationContext, RatingUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pendingIntent)

        prefs.writeAlarm(alarmManager.toString())
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

    override fun onRefresh() {
        UserLoader.loadUsers(CwApp.app.userDao.getAll()) { runOnUiThread { swiperefresh.isRefreshing = false } }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                startActivity(Intent(this, AddUserActivity::class.java))
            }
            else -> {
            }
        }
    }

}
