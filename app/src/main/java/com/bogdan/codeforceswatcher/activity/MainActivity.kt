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
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.fragment.ContestsFragment
import com.bogdan.codeforceswatcher.fragment.UsersFragment
import com.bogdan.codeforceswatcher.receiver.RatingUpdateReceiver
import com.bogdan.codeforceswatcher.util.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var counterIcon: Int = 0
    private val prefs = Prefs(this)

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
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(UsersFragment(), "Users")
        adapter.addFragment(ContestsFragment(), "Contests")
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        fab.show()
                        spSort.visibility = View.VISIBLE
                    }
                    1 -> {
                        fab.hide()
                        spSort.visibility = View.INVISIBLE
                    }
                }
            }

        })

        tabs.setupWithViewPager(viewPager)
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
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}