package com.bogdan.codeforceswatcher.util

import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.RatingChangeResponse
import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

object UserLoader {

    fun loadUsers(roomUserList: List<User> = CwApp.app.userDao.getAll(), shouldDisplayErrors: Boolean, userLoaded: (MutableList<Pair<String, Int>>) -> Unit = {}) {
        val userCall = CwApp.app.codeforcesApi.getUsers(getHandles(roomUserList))
        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.body() == null) {
                    userLoaded(mutableListOf())
                    if (shouldDisplayErrors)
                        showError(CwApp.app.getString(R.string.failed_to_fetch_users))
                } else {
                    val userList = response.body()?.result
                    if (userList != null) {
                        loadRatingUpdates(roomUserList, userList, userLoaded)
                    } else {
                        userLoaded(mutableListOf())
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                userLoaded(mutableListOf())
                if (shouldDisplayErrors)
                    showError()
            }
        })
    }

    private fun loadRatingUpdates(
            roomUserList: List<User>,
            userList: List<User>,
            userLoaded: (MutableList<Pair<String, Int>>) -> Unit
    ) {
        Thread {
            val result: MutableList<Pair<String, Int>> = mutableListOf()

            for ((counter, element) in userList.withIndex()) {
                val response = CwApp.app.codeforcesApi.getRating(element.handle).execute()
                element.id = roomUserList[counter].id
                if (response.isSuccessful) {
                    val ratingChanges = response.body()?.result
                    if (ratingChanges != roomUserList[counter].ratingChanges) {
                        val ratingChange = ratingChanges?.lastOrNull()
                        ratingChange?.let {
                            val delta = ratingChange.newRating - ratingChange.oldRating
                            result.add(Pair(element.handle, delta))
                            element.ratingChanges = ratingChanges
                            CwApp.app.userDao.update(element)
                        }
                    }
                }
            }
            userLoaded(result)
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

}
