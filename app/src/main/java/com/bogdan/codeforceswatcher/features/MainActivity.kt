package com.bogdan.codeforceswatcher.features

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.actions.ActionsFragment
import com.bogdan.codeforceswatcher.features.add_user.AddUserActivity
import com.bogdan.codeforceswatcher.features.contests.ContestsFragment
import com.bogdan.codeforceswatcher.features.problems.ProblemsFragment
import com.bogdan.codeforceswatcher.features.users.UsersFragment
import com.bogdan.codeforceswatcher.ui.AppRateDialog
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.Prefs
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import io.xorum.codeforceswatcher.features.problems.redux.actions.ProblemsActions
import kotlinx.android.synthetic.main.activity_main.*
import redux.store

class MainActivity : AppCompatActivity() {

    private val currentTabFragment: Fragment?
        get() = supportFragmentManager.fragments.lastOrNull()

    private var searchViewItem: MenuItem? = null

    private var selectedHomeTab = HomeTab.USERS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Prefs.get().checkRateDialog()) showAppRateDialog()
        initViews()
    }

    private fun updateFragment() {
        val bottomNavSelectedItemId = selectedHomeTab.menuItemId

        tvPageTitle.text = getString(selectedHomeTab.titleId)
        toolbar.collapseActionView()

        if (bottomNavigation.selectedItemId != bottomNavSelectedItemId) {
            bottomNavigation.selectedItemId = bottomNavSelectedItemId
        }

        onNewTabSelected()

        val fragment: Fragment = when (selectedHomeTab) {
            HomeTab.USERS -> {
                currentTabFragment as? UsersFragment ?: UsersFragment()
            }
            HomeTab.CONTESTS -> {
                currentTabFragment as? ContestsFragment ?: ContestsFragment()
            }
            HomeTab.ACTIONS -> {
                currentTabFragment as? ActionsFragment ?: ActionsFragment()
            }
            HomeTab.PROBLEMS -> {
                currentTabFragment as? ProblemsFragment ?: ProblemsFragment()
            }
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    private fun onNewTabSelected() {
        when (selectedHomeTab) {
            HomeTab.USERS -> {
                onUsersTabSelected()
            }
            HomeTab.CONTESTS -> {
                onContestsTabSelected()
            }
            HomeTab.ACTIONS -> {
                onActionsTabSelected()
            }
            HomeTab.PROBLEMS -> {
                onProblemsTabSelected()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        searchViewItem = menu?.findItem(R.id.action_search)
        return super.onCreateOptionsMenu(menu)
    }

    private fun onUsersTabSelected() {
        llSorting.visibility = View.VISIBLE
        searchViewItem?.isVisible = false

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddUserActivity::class.java))
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_plus))
    }

    private fun onContestsTabSelected() {
        llSorting.visibility = View.GONE
        searchViewItem?.isVisible = false

        fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(CONTESTS_LINK))
            startActivity(intent)
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_eye))
    }

    private fun onActionsTabSelected() {
        llSorting.visibility = View.GONE
        searchViewItem?.isVisible = false

        fab.setOnClickListener {
            showShareDialog()
            Analytics.logShareApp()
        }
        fab.setImageDrawable(getDrawable(R.drawable.ic_share))
    }

    private fun onProblemsTabSelected() {
        llSorting.visibility = View.GONE
        searchViewItem?.isVisible = true

        var problemsIsFavourite = store.state.problems.isFavourite
        updateProblemsFAB(problemsIsFavourite)

        fab.setOnClickListener {
            problemsIsFavourite = !(problemsIsFavourite)

            store.dispatch(ProblemsActions.ChangeTypeProblems(problemsIsFavourite))
            updateProblemsFAB(problemsIsFavourite)
        }
    }

    private fun updateProblemsFAB(problemsIsFavourite: Boolean) {
        if (problemsIsFavourite) {
            fab.setImageDrawable(getDrawable(R.drawable.ic_all))
        } else {
            fab.setImageDrawable(getDrawable(R.drawable.ic_star))
        }
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

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        spSort.background.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                PorterDuff.Mode.SRC_ATOP
        )

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val homeTab = HomeTab.fromMenuItemId(item.itemId)
            if (homeTab != selectedHomeTab) {
                selectedHomeTab = homeTab
                updateFragment()
            }
            true
        }

        updateFragment()

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
        private const val CONTESTS_LINK = "http://codeforces.com/contests"
    }

    enum class HomeTab(val titleId: Int, val menuItemId: Int) {

        USERS(R.string.empty, R.id.navUsers),
        CONTESTS(R.string.contests, R.id.navContests),
        ACTIONS(R.string.actions, R.id.navActions),
        PROBLEMS(R.string.problems, R.id.navProblems);

        companion object {

            fun fromMenuItemId(menuItemId: Int): HomeTab =
                    enumValues<HomeTab>().find { it.menuItemId == menuItemId } ?: USERS
        }
    }
}
