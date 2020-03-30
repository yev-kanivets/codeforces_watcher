package io.xorum.codeforceswatcher.util

import io.xorum.codeforceswatcher.redux.states.AppState
import io.xorum.codeforceswatcher.redux.store
import tw.geothings.rekotlin.StoreSubscriber

class PersistenceController : StoreSubscriber<AppState> {

    fun onAppCreated() {
        store.subscribe(this) {
            it.skipRepeats { oldState, newState ->
                oldState.users.sortType == newState.users.sortType &&
                        oldState.problems.isFavourite == newState.problems.isFavourite
            }
        }
    }

    override fun newState(state: AppState) {
        settings.writeSpinnerSortPosition(state.users.sortType.position)
        settings.writeProblemsIsFavourite(state.problems.isFavourite)
    }
}
