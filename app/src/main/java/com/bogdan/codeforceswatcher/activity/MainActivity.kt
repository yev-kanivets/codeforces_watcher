package com.bogdan.codeforceswatcher.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bogdan.codeforceswatcher.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private val users = mutableListOf<User>()
    lateinit var it: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener(this)

        val userAdapter = UserAdapter(users, this)

        rvMain.adapter = userAdapter

        swiperefresh.setOnRefreshListener(this)

        rvMain.layoutManager = LinearLayoutManager(this)

        val liveData = CwApp.app.userDao.getAll()

        liveData.observe(this, Observer<List<User>> { t ->
            users.clear()
            it = t!!
            for (element in t.size - 1 downTo 0) {
                users.add(t[element])
                userAdapter.notifyDataSetChanged()
            }
            userAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        const val ID = "Id"
    }

    private fun loadUser(handle: String) {
        val userCall = CwApp.app.userApi.user(handle)
        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val countDownLatch = CountDownLatch(response.body()!!.result.size)
                Thread {
                    countDownLatch.await()
                    runOnUiThread {
                        swiperefresh.isRefreshing = false
                    }

                }.start()
                for ((counter, element) in response.body()!!.result.withIndex()) {
                    val ratingCall = CwApp.app.userApi.rating(element.handle)
                    element.id = it[counter].id
                    if (element.rating == it[counter].rating) {
                        element.ratingChanges = it[counter].ratingChanges
                        CwApp.app.userDao.update(element)
                        countDownLatch.countDown()
                    } else {
                        ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                            override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                                if (response.isSuccessful) {
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
                swiperefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                swiperefresh.isRefreshing = false
                CwApp.app.showError()
            }
        })
    }

    override fun onRefresh() {
        var handles = ""
        for (element in this.it) {
            handles += element.handle + ";"
        }
        loadUser(handles)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                startActivity(Intent(this, AddUserActivity::class.java))
            }
            else -> {
            }
        }
    }

}