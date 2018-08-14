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
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        counterIcon = if (prefs.readCounter().isEmpty()) 0 else prefs.readCounter().toInt()

        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
        }

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        counterIcon = (counterIcon + 2) % 3
        showItemMenu(menu.findItem(R.id.action_descending_ascending))
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_descending_ascending -> {
                showItemMenu(item)
                prefs.writeCounter(counterIcon)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        fab.setOnClickListener(this)
        swiperefresh.setOnRefreshListener(this)

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
            userList?.let {
                when (counterIcon) {
                    0 -> userAdapter.setItems(it.reversed())
                    1 -> userAdapter.setItems(it.sortedBy(User::rating))
                    2 -> userAdapter.setItems(it.sortedByDescending(User::rating))
                }
            }
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

    private fun showItemMenu(item: MenuItem) {
        val users = CwApp.app.userDao.getAll()
        when (counterIcon) {
            0 -> {
                userAdapter.setItems(users.sortedBy { it.rating })
                item.icon = resources.getDrawable(R.drawable.ic_sort_descending_white)
            }
            1 -> {
                userAdapter.setItems(users.sortedByDescending { it.rating })
                item.icon = resources.getDrawable(R.drawable.ic_sort_ascending)
            }
            2 -> {
                userAdapter.setItems(users.reversed())
                item.icon = resources.getDrawable(R.drawable.ic_sort_descending_grey)
            }
        }
        counterIcon = (counterIcon + 1) % 3
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
