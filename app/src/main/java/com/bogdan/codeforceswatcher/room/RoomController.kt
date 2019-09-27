package com.bogdan.codeforceswatcher.room

import com.bogdan.codeforceswatcher.features.contests.redux.states.ContestsState
import com.bogdan.codeforceswatcher.features.users.redux.states.UsersState
import com.bogdan.codeforceswatcher.redux.AppState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Prefs
import org.rekotlin.StoreSubscriber

object RoomController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests
            }
        }
    }

    fun fetchAppState(): AppState {
        return AppState(
            contests = ContestsState(contests = DatabaseClient.contestDao.getUpcomingContests()),
            users = UsersState(
                users = DatabaseClient.userDao.getAll(),
                sortType = UsersState.SortType.getSortType(Prefs.get().readSpinnerSortPosition().toInt())
            )
        )
    }

    override fun newState(state: AppState) {
        DatabaseClient.contestDao.deleteAll()
        DatabaseClient.contestDao.insert(state.contests.contests)
    }
}
