package io.xorum.codeforceswatcher.db

import io.xorum.codeforceswatcher.features.problems.redux.states.ProblemsState
import io.xorum.codeforceswatcher.features.contests.redux.states.ContestsState
import io.xorum.codeforceswatcher.features.users.redux.states.UsersState
import io.xorum.codeforceswatcher.redux.states.AppState
import io.xorum.codeforceswatcher.redux.store
import tw.geothings.rekotlin.StoreSubscriber

interface SavedData {
    fun readSpinnerSortPosition(): Int
    fun readProblemsIsFavourite(): Boolean
}

var savedData: SavedData? = null

object DatabaseController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests
            }
        }
    }

    fun fetchAppState() = AppState(
            contests = ContestsState(contests = DatabaseQueries.Contests.getAll()),
            users = UsersState(
                    users = DatabaseQueries.Users.getAll(),
                    sortType = UsersState.SortType.getSortType(savedData?.readSpinnerSortPosition()
                            ?: 0)
            ),
            problems = ProblemsState(
                    problems = DatabaseQueries.Problems.getAll(),
                    isFavourite = (savedData?.readProblemsIsFavourite() ?: false)
            )
    )

    override fun newState(state: AppState) {
        if (DatabaseQueries.Contests.getAll() != state.contests.contests.sortedBy { it.id }) {
            DatabaseQueries.Contests.deleteAll()
            DatabaseQueries.Contests.insert(state.contests.contests)
        }
    }
}
