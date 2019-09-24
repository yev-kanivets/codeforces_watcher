package com.bogdan.codeforceswatcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersRequests
import com.bogdan.codeforceswatcher.store

class RatingUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        store.dispatch(UsersRequests.FetchUsers(isInitiatedByUser = false))
    }

}
