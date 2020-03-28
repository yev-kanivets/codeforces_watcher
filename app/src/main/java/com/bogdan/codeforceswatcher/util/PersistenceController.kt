package com.bogdan.codeforceswatcher.util

import io.xorum.codeforceswatcher.redux.states.AppState
import io.xorum.codeforceswatcher.redux.store
import tw.geothings.rekotlin.StoreSubscriber

object PersistenceController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.users.sortType == newState.users.sortType &&
                        oldState.problems.isFavourite == newState.problems.isFavourite
            }
        }
    }

    override fun newState(state: AppState) {
        Prefs.get().writeSpinnerSortPosition(state.users.sortType.position)
        Prefs.get().writeProblemsIsFavourite(state.problems.isFavourite)
    }
}
