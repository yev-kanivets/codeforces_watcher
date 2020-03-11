package com.bogdan.codeforceswatcher.room

import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.problems.redux.states.ProblemsState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import com.bogdan.codeforceswatcher.redux.states.AppState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Prefs
import io.xorum.codeforceswatcher.db.DatabaseQueries
import tw.geothings.rekotlin.StoreSubscriber

object DatabaseController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests
            }
        }
    }

    fun fetchAppState(): AppState {
        return AppState(
                contests = ContestsState(contests = DatabaseQueries.Contests.getAll()),
                users = UsersState(
                        users = DatabaseQueries.Users.getAll(),
                        sortType = UsersState.SortType.getSortType(Prefs.get().readSpinnerSortPosition().toInt())
                ),
                problems = ProblemsState(
                        problems = DatabaseQueries.Problems.getAll(),
                        isFavourite = (Prefs.get().readProblemsIsFavourite())
                )
        )
    }

    override fun newState(state: AppState) {
        if (DatabaseQueries.Contests.getAll() != state.contests.contests.sortedBy { it.id }) {
            DatabaseQueries.Contests.deleteAll()
            DatabaseQueries.Contests.insert(state.contests.contests)
        }
    }
}
