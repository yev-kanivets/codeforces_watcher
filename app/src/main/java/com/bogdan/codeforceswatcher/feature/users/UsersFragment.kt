package com.bogdan.codeforceswatcher.feature.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.adapter.UserAdapter
import com.bogdan.codeforceswatcher.feature.users.redux.UsersState
import com.bogdan.codeforceswatcher.feature.users.redux.actions.SortActions
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersRequests
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Analytics
import com.bogdan.codeforceswatcher.util.Prefs
import kotlinx.android.synthetic.main.fragment_users.*
import org.rekotlin.StoreSubscriber

class UsersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    StoreSubscriber<UsersState> {

    private val userAdapter by lazy { UserAdapter(listOf(), requireContext()) }

    private var sortPosition: Int = 0
    private lateinit var spSort: AppCompatSpinner
    private var prefs = Prefs.get()

    override fun onRefresh() {
        store.dispatch(UsersRequests.FetchUsers(isUser = true))

        Analytics.logUsersListRefresh()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this) { state -> state.select { it.users } }
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: UsersState) {
        swipeToRefresh.isRefreshing = (state.status == UsersState.Status.PENDING)
        userAdapter.setItems(state.users)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sortPosition = prefs.readCounter().toInt()
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
        swipeToRefresh.setOnRefreshListener(this)

        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        spSort = requireActivity().findViewById(R.id.spSort)

        val spinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item,
            resources.getStringArray(R.array.array_sort)
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spSort.adapter = spinnerAdapter
        spSort.setSelection(sortPosition)

        spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortPosition = position
                prefs.writeCounter(position)
                store.dispatch(SortActions.Sort(getSortTypeFromPosition(position)))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getSortTypeFromPosition(sortType: Int) =
        when (sortType) {
            0 -> UsersState.Sort.DEFAULT
            1 -> UsersState.Sort.RATING_DOWN
            2 -> UsersState.Sort.RATING_UP
            3 -> UsersState.Sort.UPDATE_DOWN
            4 -> UsersState.Sort.UPDATE_UP
            else -> UsersState.Sort.DEFAULT
        }
}
