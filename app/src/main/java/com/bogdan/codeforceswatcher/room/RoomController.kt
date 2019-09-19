package com.bogdan.codeforceswatcher.room

import com.bogdan.codeforceswatcher.feature.contests.redux.ContestsState
import com.bogdan.codeforceswatcher.redux.AppState
import com.bogdan.codeforceswatcher.store
import org.rekotlin.StoreSubscriber

object RoomController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.contests == newState.contests
            }
        }
    }

    fun fetchAppState() = AppState(
        contests = ContestsState(contests = DatabaseClient.contestDao.getUpcomingContests())
    )

    override fun newState(state: AppState) {
        DatabaseClient.contestDao.deleteAll()
        DatabaseClient.contestDao.insert(state.contests.contests)
    }
}
