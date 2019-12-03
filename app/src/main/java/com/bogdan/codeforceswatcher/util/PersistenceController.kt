package com.bogdan.codeforceswatcher.util

import com.bogdan.codeforceswatcher.redux.states.AppState
import com.bogdan.codeforceswatcher.store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.rekotlin.StoreSubscriber

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
