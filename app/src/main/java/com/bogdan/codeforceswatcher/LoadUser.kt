package com.bogdan.codeforceswatcher

import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import com.bogdan.codeforceswatcher.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

object LoadUser {

    var textNotification = ""

    fun loadUser(handle: String, swipeRefresh: SwipeRefreshLayout?) {
        var flag = 0
        val userCall = CwApp.app.userApi.user(handle)
        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.body() == null) {
                    if (swipeRefresh != null) {
                        swipeRefresh.isRefreshing = false
                    }
                } else {
                    val countDownLatch = CountDownLatch(response.body()!!.result.size)
                    if (swipeRefresh != null) {
                        Thread {
                            countDownLatch.await()
                            MainActivity.run {
                                swipeRefresh.isRefreshing = false
                            }
                        }.start()
                    }
                    for ((counter, element) in response.body()!!.result.withIndex()) {
                        val ratingCall = CwApp.app.userApi.rating(element.handle)
                        element.id = MainActivity.it[counter].id
                        if (element.rating == MainActivity.it[counter].rating) {
                            element.ratingChanges = MainActivity.it[counter].ratingChanges
                            CwApp.app.userDao.update(element)
                            if (swipeRefresh != null) {
                                countDownLatch.countDown()
                            }
                        } else {
                            if (flag == 1) {
                                textNotification += "\n"
                            }
                            textNotification += element.handle + " "
                            ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                                override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                                    if (response.isSuccessful) {
                                        val delta = element.ratingChanges.lastOrNull()!!.newRating - element.ratingChanges.lastOrNull()!!.oldRating
                                        textNotification += if (delta < 0) {
                                            "-" + delta.toString()
                                        } else {
                                            "+" + delta.toString()
                                        }
                                        flag = 1
                                        element.ratingChanges = response.body()!!.result
                                        CwApp.app.userDao.update(element)
                                        if (swipeRefresh != null) {
                                            countDownLatch.countDown()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {
                                    if (swipeRefresh != null) {
                                        countDownLatch.countDown()
                                    }
                                }
                            })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if (swipeRefresh != null) {
                    swipeRefresh.isRefreshing = false
                }
                CwApp.app.showError()
            }
        })
    }

}