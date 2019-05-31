package com.bogdan.codeforceswatcher.util

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

    fun loadUsers(roomUserList: List<User>, shouldDisplayErrors: Boolean, userLoaded: (MutableList<Pair<String, Int>>) -> Unit) {
        val userCall = CwApp.app.userApi.user(getHandles(roomUserList))
        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.body() == null) {
                    userLoaded(mutableListOf())
                    if (shouldDisplayErrors)
                        CwApp.app.showError(CwApp.app.getString(R.string.failed_to_fetch_users))
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
                    CwApp.app.showError()
            }
        })
    }

    private fun loadRatingUpdates(
            roomUserList: List<User>,
            userList: List<User>,
            userLoaded: (MutableList<Pair<String, Int>>) -> Unit
    ) {
        val result: MutableList<Pair<String, Int>> = mutableListOf()
        val countDownLatch = CountDownLatch(userList.size)

        Thread {
            countDownLatch.await()
            userLoaded(result)
        }.start()

        for ((counter, element) in userList.withIndex()) {
            val ratingCall = CwApp.app.userApi.rating(element.handle)
            element.id = roomUserList[counter].id
            if (element.rating == roomUserList[counter].rating) {
                element.ratingChanges = roomUserList[counter].ratingChanges
                CwApp.app.userDao.update(element)
                countDownLatch.countDown()
            } else {
                ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                    override fun onResponse(
                            call: Call<RatingChangeResponse>,
                            response: Response<RatingChangeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val ratingChanges = response.body()?.result
                            val ratingChange = ratingChanges?.lastOrNull()
                            ratingChange?.let {
                                val delta = ratingChange.newRating - ratingChange.oldRating
                                result.add(Pair(element.handle, delta))
                                element.ratingChanges = ratingChanges
                                CwApp.app.userDao.update(element)
                            }
                        }
                        countDownLatch.countDown()
                    }

                    override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {
                        countDownLatch.countDown()
                    }
                })
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

}
