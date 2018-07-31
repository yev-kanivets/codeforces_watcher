package com.bogdan.codeforceswatcher

import com.bogdan.codeforceswatcher.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

object UserLoaded {

    fun loadUsers(handle: String, userLoaded: (MutableList<Pair<String, Int>>?) -> Unit) {
        val ratingChanges: MutableList<Pair<String, Int>>? = null
        val userCall = CwApp.app.userApi.user(handle)
        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.body() == null) {
                    userLoaded(ratingChanges)
                } else {
                    val countDownLatch = CountDownLatch(response.body()!!.result.size)
                    Thread {
                        countDownLatch.await()
                        userLoaded(ratingChanges)
                    }.start()
                    for ((counter, element) in response.body()!!.result.withIndex()) {
                        val ratingCall = CwApp.app.userApi.rating(element.handle)
                        element.id = MainActivity.it[counter].id
                        if (element.rating == MainActivity.it[counter].rating) {
                            element.ratingChanges = MainActivity.it[counter].ratingChanges
                            CwApp.app.userDao.update(element)
                            countDownLatch.countDown()
                        } else {
                            ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                                override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                                    if (response.isSuccessful) {
                                        val delta = element.ratingChanges.lastOrNull()!!.newRating - element.ratingChanges.lastOrNull()!!.oldRating
                                        ratingChanges?.add(Pair(element.handle, delta))
                                        element.ratingChanges = response.body()!!.result
                                        CwApp.app.userDao.update(element)
                                        countDownLatch.countDown()
                                    }
                                }

                                override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {
                                    countDownLatch.countDown()
                                }
                            })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                userLoaded(ratingChanges)
                CwApp.app.showError()
            }
        })
    }

}
