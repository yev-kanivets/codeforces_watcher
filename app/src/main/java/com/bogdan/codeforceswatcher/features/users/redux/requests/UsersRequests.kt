package com.bogdan.codeforceswatcher.features.users.redux.requests

import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
import com.bogdan.codeforceswatcher.redux.ToastAction
import com.bogdan.codeforceswatcher.room.DatabaseClient
import com.bogdan.codeforceswatcher.store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.rekotlin.Action
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRequests {

    class FetchUsers(
        private val isInitiatedByUser: Boolean
    ) : Request() {

        override fun execute() {
            val users: List<User> = DatabaseClient.userDao.getAll()

            val userCall = RestClient.getUsers(getHandles(users))
            userCall.enqueue(object : Callback<UsersResponse> {

                val failedToFetchUsersError = CwApp.app.getString(R.string.failed_to_fetch_users)

                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    if (response.body() == null) {
                        store.dispatch(Failure(if (isInitiatedByUser) failedToFetchUsersError else null))
                    } else {
                        val userList = response.body()?.users
                        if (userList != null) {
                            loadRatingUpdates(users, userList)
                        } else {
                            store.dispatch(Failure(if (isInitiatedByUser) failedToFetchUsersError else null))
                        }
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    store.dispatch(Failure(if (isInitiatedByUser) CwApp.app.resources.getString(R.string.no_internet_connection) else null))
                }
            })
        }

        private fun loadRatingUpdates(
            roomUserList: List<User>,
            userList: List<User>
        ) {
            Thread {
                val notificationData: MutableList<Pair<String, Int>> = mutableListOf()

                for ((counter, element) in userList.withIndex()) {
                    val response = RestClient.getRating(element.handle).execute()
                    element.id = roomUserList[counter].id
                    element.ratingChanges = roomUserList[counter].ratingChanges
                    if (response.isSuccessful) {
                        val ratingChanges = response.body()?.ratingChanges
                        if (ratingChanges != roomUserList[counter].ratingChanges) {
                            val ratingChange = ratingChanges?.lastOrNull()
                            ratingChange?.let {
                                val delta = ratingChange.newRating - ratingChange.oldRating
                                notificationData.add(Pair(element.handle, delta))
                                element.ratingChanges = ratingChanges
                                DatabaseClient.userDao.update(element)
                            }
                        }
                    }
                }

                dispatchSuccess(userList, notificationData)
            }.start()

        }

        private fun dispatchSuccess(userList: List<User>, result: List<Pair<String, Int>>) {
            runBlocking {
                withContext(Dispatchers.Main) {
                    store.dispatch(Success(userList, result, isInitiatedByUser))
                }
            }
        }

        private fun getHandles(roomUserList: List<User>): String {
            var handles = ""
            for (element in roomUserList) {
                handles += element.handle + ";"
            }
            return handles
        }

        data class Success(val users: List<User>, val notificationData: List<Pair<String, Int>>, val isUserInitiated: Boolean) : Action

        data class Failure(override val message: String?) : ToastAction
    }

}