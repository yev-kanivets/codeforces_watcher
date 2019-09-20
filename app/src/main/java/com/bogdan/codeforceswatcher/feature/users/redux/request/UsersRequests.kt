package com.bogdan.codeforceswatcher.feature.users.redux.request

import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.redux.Request
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
        private val users: List<User> = DatabaseClient.userDao.getAll(),
        private val isUser: Boolean
    ) : Request() {

        override fun execute() {
            val userCall = RestClient.getUsers(getHandles(users))
            userCall.enqueue(object : Callback<UsersResponse> {

                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    if (response.body() == null) {
                        store.dispatch(Success(listOf(), listOf(), isUser))
                        //if (isUser) showError(CwApp.app.getString(R.string.failed_to_fetch_users))
                    } else {
                        val userList = response.body()?.result
                        if (userList != null) {
                            loadRatingUpdates(users, userList)
                        } else {
                            store.dispatch(Success(listOf(), listOf(), isUser))
                        }
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    store.dispatch(Failure())
                        //if (isUser) showError()
                }
            })
        }

        private fun loadRatingUpdates(
            roomUserList: List<User>,
            userList: List<User>
        ) {
            Thread {
                val result: MutableList<Pair<String, Int>> = mutableListOf()

                for ((counter, element) in userList.withIndex()) {
                    val response = RestClient.getRating(element.handle).execute()
                    element.id = roomUserList[counter].id
                    element.ratingChanges = roomUserList[counter].ratingChanges
                    if (response.isSuccessful) {
                        val ratingChanges = response.body()?.result
                        if (ratingChanges != roomUserList[counter].ratingChanges) {
                            val ratingChange = ratingChanges?.lastOrNull()
                            ratingChange?.let {
                                val delta = ratingChange.newRating - ratingChange.oldRating
                                result.add(Pair(element.handle, delta))
                                element.ratingChanges = ratingChanges
                                DatabaseClient.userDao.update(element)
                            }
                        }
                    }
                }
                runBlocking {
                    withContext(Dispatchers.Main) {
                        store.dispatch(Success(userList, result, isUser))
                    }
                }
            }.start()

        }

        private fun getHandles(roomUserList: List<User>): String {
            var handles = ""
            for (element in roomUserList) {
                handles += element.handle + ";"
            }
            return handles
        }

        private fun showError(message: String = CwApp.app.resources.getString(R.string.no_internet_connection)) {
            Toast.makeText(CwApp.app, message, Toast.LENGTH_SHORT).show()
        }

        data class Success(val users: List<User>, val result: List<Pair<String, Int>>, val isUser: Boolean) : Action

        data class Failure(val t: Throwable? = null) : Action
    }
}
