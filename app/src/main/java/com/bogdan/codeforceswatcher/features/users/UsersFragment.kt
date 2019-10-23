package com.bogdan.codeforceswatcher.features.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.features.users.redux.actions.UsersActions
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState.SortType.Companion.getSortType
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersRequests
import com.bogdan.codeforceswatcher.features.users.models.User
import com.bogdan.codeforceswatcher.features.users.redux.requests.Source
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.fragment_users_stub.*
import kotlinx.android.synthetic.main.fragment_users.*
import org.rekotlin.StoreSubscriber

class UsersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    StoreSubscriber<UsersState> {

    private val usersAdapter by lazy { UserAdapter(requireContext()) }

    private lateinit var spSort: AppCompatSpinner

    override fun onRefresh() {
        store.dispatch(UsersRequests.FetchUsers(Source.USER))

        Analytics.logUsersListRefresh()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state ->
            state.skipRepeats { oldState, newState -> oldState.users == newState.users }
                .select { it.users }
        }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UsersState) {
        swipeRefreshLayout.isRefreshing = (state.status == UsersState.Status.PENDING)
        usersAdapter.setItems(state.users.sort(state.sortType))
        adjustNoUsersStubVisibility(state.users.isEmpty())
    }

    private fun adjustNoUsersStubVisibility(isUsersListEmpty: Boolean) {
        ivAlien.visibility = if (isUsersListEmpty) View.VISIBLE else View.GONE
        tvNoUsers.visibility = if (isUsersListEmpty) View.VISIBLE else View.GONE
        spSort.visibility = if (isUsersListEmpty) View.GONE else View.VISIBLE
        requireActivity().findViewById<TextView>(R.id.tvSortBy).visibility =
            if (isUsersListEmpty) View.GONE else View.VISIBLE

        swipeRefreshLayout.isEnabled = !isUsersListEmpty
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        swipeRefreshLayout.setOnRefreshListener(this)

        recyclerView.adapter = usersAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        spSort = requireActivity().findViewById(R.id.spSort)

        val spinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item,
            resources.getStringArray(R.array.array_sort)
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spSort.adapter = spinnerAdapter
        spSort.setSelection(store.state.users.sortType.position)

        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                store.dispatch(UsersActions.Sort(getSortType(position)))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun List<User>.sort(sortType: UsersState.SortType) = when (sortType) {
        UsersState.SortType.DEFAULT -> reversed()
        UsersState.SortType.RATING_DOWN -> sortedByDescending(User::rating)
        UsersState.SortType.RATING_UP -> sortedBy(User::rating)
        UsersState.SortType.UPDATE_DOWN -> sortedByDescending { user ->
            user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
        }
        UsersState.SortType.UPDATE_UP -> sortedBy { user ->
            user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
        }
    }

}
