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
import android.view.View
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.receiver.RatingUpdateReceiver
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val sharedPrefs = getPreferences(MODE_PRIVATE)

        val savedText = sharedPrefs.getString(SAVED_TEXT, "")
        if (savedText == "") {
            startAlarm()
        }

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun initViews() {
        fab.setOnClickListener(this)
        swiperefresh.setOnRefreshListener(this)

        val userAdapter = UserAdapter(users, this)

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
            users.clear()
            userList?.let { users.addAll(it.reversed()) }
            userAdapter.notifyDataSetChanged()
        })
    }

    private fun startAlarm() {
        val intent = Intent(applicationContext, RatingUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pendingIntent)

        val sharedPrefs = getPreferences(MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(SAVED_TEXT, alarmManager.toString())
        editor.apply()
    }

    override fun onRefresh() {
        UserLoader.loadUsers(users) { runOnUiThread { swiperefresh.isRefreshing = false } }
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

    companion object {
        private const val SAVED_TEXT = "saved_text"
    }

}
