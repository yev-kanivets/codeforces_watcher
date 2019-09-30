package com.bogdan.codeforceswatcher.features

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
import com.bogdan.codeforceswatcher.features.add_user.AddUserActivity
import com.bogdan.codeforceswatcher.features.contests.ContestsFragment
import com.bogdan.codeforceswatcher.features.users.UsersFragment
import com.bogdan.codeforceswatcher.receiver.StartAlarm
import com.bogdan.codeforceswatcher.redux.actions.UIActions
import com.bogdan.codeforceswatcher.redux.states.UIState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.ui.AppRateDialog
import com.bogdan.codeforceswatcher.util.Prefs
import kotlinx.android.synthetic.main.activity_main.bottomNavigation
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.llToolbar
import kotlinx.android.synthetic.main.activity_main.spSort
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.viewPager
import org.rekotlin.StoreSubscriber

class MainActivity : AppCompatActivity(), StoreSubscriber<UIState> {

    private val prefs = Prefs.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state -> state.select { it.ui } }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UIState) {
        when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> {
                viewPager.currentItem = 0
                llToolbar.visibility = View.VISIBLE
                fab.setOnClickListener {
                    val intent =
                        Intent(this@MainActivity, AddUserActivity::class.java)
                    startActivity(intent)
                }
                fab.setImageDrawable(getDrawable(R.drawable.ic_plus))
            }
            UIState.HomeTab.CONTESTS -> {
                viewPager.currentItem = 1
                llToolbar.visibility = View.GONE
                fab.setOnClickListener {
                    val intent =
                        Intent(Intent.ACTION_VIEW).setData(Uri.parse(CODEFORCES_LINK))
                    startActivity(intent)
                }
                fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
            }
        }
    }

    private fun initData() {
        if (prefs.readAlarm().isEmpty()) {
            startAlarm()
            prefs.writeAlarm("alarm")
        }

        prefs.addLaunchCount()
        if (prefs.checkRateDialog()) showAppRateDialog()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        val viewPagerListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigation.selectedItemId = R.id.navUsers
                    1 -> bottomNavigation.selectedItemId = R.id.navContests
                }
            }
        }

        viewPagerListener.onPageSelected(0)
        viewPager.addOnPageChangeListener(viewPagerListener)

        spSort.background.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val homeTab = UIState.HomeTab.fromMenuItemId(item.itemId)
            if (homeTab != store.state.ui.selectedHomeTab) {
                store.dispatch(UIActions.SelectHomeTab(homeTab))
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

    class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
