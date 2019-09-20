package com.bogdan.codeforceswatcher.room

import com.bogdan.codeforceswatcher.feature.contests.redux.ContestsState
import com.bogdan.codeforceswatcher.feature.users.redux.Sorting
import com.bogdan.codeforceswatcher.feature.users.redux.UsersState
import com.bogdan.codeforceswatcher.redux.AppState
import com.bogdan.codeforceswatcher.store
import com.bogdan.codeforceswatcher.util.Prefs
import org.rekotlin.StoreSubscriber

object RoomController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests &&
                    oldState.users == newState.users
            }
        }
    }

    fun fetchAppState(): AppState {
        val sortType = UsersState.SortType.getSortTypeFromPosition(Prefs.get().readSpinnerSortPosition().toInt())

        return AppState(
            contests = ContestsState(contests = DatabaseClient.contestDao.getUpcomingContests()),
            users = UsersState(
                users = Sorting.sort(DatabaseClient.userDao.getAll(), sortType),
                sortType = sortType)
        )
    }

    override fun newState(state: AppState) {
        DatabaseClient.contestDao.deleteAll()
        DatabaseClient.contestDao.insert(state.contests.contests)
    }
}
