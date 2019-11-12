package com.bogdan.codeforceswatcher.features

import android.app.AlertDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.ActionsFragment
import com.bogdan.codeforceswatcher.features.add_user.AddUserActivity
import com.bogdan.codeforceswatcher.features.contests.ContestsFragment
import com.bogdan.codeforceswatcher.features.problems.ProblemsFragment
import com.bogdan.codeforceswatcher.features.users.UsersFragment
import com.bogdan.codeforceswatcher.redux.actions.UIActions
import com.bogdan.codeforceswatcher.redux.states.UIState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.ui.AppRateDialog
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.Prefs
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import kotlinx.android.synthetic.main.activity_main.*
import org.rekotlin.StoreSubscriber

class MainActivity : AppCompatActivity(), StoreSubscriber<UIState> {

    private val currentTabFragment: Fragment?
        get() = supportFragmentManager.fragments.lastOrNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Prefs.get().checkRateDialog()) showAppRateDialog()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState -> oldState.ui == newState.ui }
                .select { it.ui }
        }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UIState) {
        val fragment: Fragment = when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> {
                currentTabFragment as? UsersFragment ?: UsersFragment()
            }
            UIState.HomeTab.CONTESTS -> {
                currentTabFragment as? ContestsFragment ?: ContestsFragment()
            }
            UIState.HomeTab.ACTIONS -> {
                currentTabFragment as? ActionsFragment ?: ActionsFragment()
            }
            UIState.HomeTab.PROBLEMS -> {
                currentTabFragment as? ProblemsFragment ?: ProblemsFragment()
            }
        }

        if (fragment != currentTabFragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        val bottomNavSelectedItemId = when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> R.id.navUsers
            UIState.HomeTab.CONTESTS -> R.id.navContests
            UIState.HomeTab.ACTIONS -> R.id.navActions
            UIState.HomeTab.PROBLEMS -> R.id.navProblems
        }

        tvPageTitle.text = getString(state.selectedHomeTab.titleId)

        if (bottomNavigation.selectedItemId != bottomNavSelectedItemId) {
            bottomNavigation.selectedItemId = bottomNavSelectedItemId
        }

        when (state.selectedHomeTab) {
            UIState.HomeTab.USERS -> {
                onUsersTabSelected()
            }
            UIState.HomeTab.CONTESTS -> {
                onContestsTabSelected()
            }
            UIState.HomeTab.ACTIONS -> {
                onActionsTabSelected()
            }
            UIState.HomeTab.PROBLEMS -> {
                onProblemsTabSelected()
            }
        }
    }

    private fun onUsersTabSelected() {
        llSorting.visibility = View.VISIBLE
        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddUserActivity::class.java))
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_plus))
    }

    private fun onContestsTabSelected() {
        llSorting.visibility = View.GONE
        fab.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse(CODEFORCES_LINK))
            startActivity(intent)
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
    }

    private fun onActionsTabSelected() {
        llSorting.visibility = View.GONE
        fab.setOnClickListener {
            showShareDialog()
            Analytics.logShareApp()
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_share))
    }

    private fun showShareDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.share_cw))
            .setMessage(getString(R.string.help_cw_make_more_social))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.share)) { _, _ ->
                share()
                Analytics.logAppShared()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
            .show()
    }

    private fun share() = startActivity(Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_cw_message))
    })

    private fun onProblemsTabSelected() {
        llSorting.visibility = View.GONE
        fab.setOnClickListener(null)
        fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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

        val menuView = bottomNavigation.getChildAt(0) as? BottomNavigationMenuView ?: return

        for (i in 0 until menuView.childCount) {
            val activeLabel = menuView.getChildAt(i).findViewById<View>(R.id.largeLabel)
            if (activeLabel is TextView) {
                activeLabel.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun showAppRateDialog() {
        val rateDialog = AppRateDialog()
        rateDialog.isCancelable = false
        rateDialog.show(supportFragmentManager, "progressDialog")
    }

    companion object {
        private const val CODEFORCES_LINK = "http://codeforces.com/contests"
    }
}
