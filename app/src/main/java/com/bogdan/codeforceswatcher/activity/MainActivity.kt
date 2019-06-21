package com.bogdan.codeforceswatcher.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.fragment.ContestsFragment
import com.bogdan.codeforceswatcher.fragment.UsersFragment
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val prefs = Prefs.get()
    var toolbarViewGroup: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
            prefs.writeAlarm("alarm")
        }

        UserLoader.loadUsers(shouldDisplayErrors = false)

        initViews()
    }

    private fun initViews() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        spSort.background.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigation.selectedItemId = R.id.navUsers
                        llToolbar.visibility = View.VISIBLE
                    }
                    1 -> {
                        bottomNavigation.selectedItemId = R.id.navContests
                        llToolbar.visibility = View.GONE
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
        val intent = Intent(this, StartAlarm::class.java)
        sendBroadcast(intent)
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