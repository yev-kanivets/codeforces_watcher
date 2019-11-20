package com.bogdan.codeforceswatcher.room

import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.problems.redux.states.ProblemsState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import com.bogdan.codeforceswatcher.redux.states.AppState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Prefs
import org.rekotlin.StoreSubscriber

object RoomController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests
                    && oldState.problems == newState.problems
            }
        }
    }

    fun fetchAppState(): AppState {
        return AppState(
            contests = ContestsState(contests = DatabaseClient.contestDao.getUpcomingContests().reversed()),
            users = UsersState(
                users = DatabaseClient.userDao.getUsers(),
                sortType = UsersState.SortType.getSortType(Prefs.get().readSpinnerSortPosition().toInt())
            ),
            problems = ProblemsState(
                problems = DatabaseClient.problemsDao.getProblems()
            )
        )
    }

    override fun newState(state: AppState) {
        if (DatabaseClient.contestDao.getUpcomingContests() != state.contests.contests) {
            DatabaseClient.contestDao.deleteAll()
            DatabaseClient.contestDao.insert(state.contests.contests)
        }
        if (DatabaseClient.problemsDao.getProblems() != state.problems.problems) {
            DatabaseClient.problemsDao.deleteAll()
            DatabaseClient.problemsDao.insert(state.problems.problems)
        }
    }
}
