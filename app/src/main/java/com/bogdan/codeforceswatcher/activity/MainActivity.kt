package com.bogdan.codeforceswatcher.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.fragment.ContestsFragment
import com.bogdan.codeforceswatcher.fragment.UsersFragment
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.ui.AppRateDialog
import com.bogdan.codeforceswatcher.util.Prefs
import com.bogdan.codeforceswatcher.util.UserLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val prefs = Prefs.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initViews()
    }

    private fun initData() {
        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
            prefs.writeAlarm("alarm")
        }

        UserLoader.loadUsers(shouldDisplayErrors = false)

        prefs.addLaunchCount()
        if (prefs.checkRateDialog()) showAppRateDialog()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

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
                        fab.setOnClickListener {
                            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
                            startActivity(intent)
                        }
                        fab.setImageDrawable(getDrawable(R.drawable.ic_plus))
                    }
                    1 -> {
                        bottomNavigation.selectedItemId = R.id.navContests
                        llToolbar.visibility = View.GONE
                        fab.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(CODEFORCES_LINK))
                            startActivity(intent)
                        }
                        fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
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

    private fun showAppRateDialog() {
        val rateDialog = AppRateDialog()
        rateDialog.isCancelable = false
        rateDialog.show(supportFragmentManager, "progressDialog")
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

    companion object {
        private const val CODEFORCES_LINK = "http://codeforces.com/contests"
    }

}