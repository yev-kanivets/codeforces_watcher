package com.bogdan.codeforceswatcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bogdan.codeforceswatcher.features.users.redux.requests.Source
import com.bogdan.codeforceswatcher.features.users.redux.requests.UsersRequests
import com.bogdan.codeforceswatcher.store

class RatingUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        store.dispatch(UsersRequests.FetchUsers(false, Source.BROADCAST))
    }

}
