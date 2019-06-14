package com.bogdan.codeforceswatcher.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.fragment.ContestsFragment
import com.bogdan.codeforceswatcher.fragment.UsersFragment
import com.bogdan.codeforceswatcher.receiver.RatingUpdateReceiver
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val prefs = Prefs(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
        }

        UserLoader.loadUsers(shouldDisplayErrors = false)

        initViews()
    }

    private fun initViews() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        spSort.visibility = View.VISIBLE
                        bottomNavigation.selectedItemId = R.id.navUsers
                    }
                    1 -> {
                        bottomNavigation.selectedItemId = R.id.navContests
                        spSort.visibility = View.INVISIBLE
                    }
                }
            }

        })

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navUsers -> viewPager.currentItem = 0
                R.id.navContests -> viewPager.currentItem = 1
            }
            true
        }
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

    class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = listOf<Fragment>(UsersFragment(), ContestsFragment())
        private val mFragmentTitleList = listOf("Users", "Contests")

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}