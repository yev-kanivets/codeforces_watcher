package com.bogdan.codeforceswatcher

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.arch.persistence.room.TypeConverters
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private val users = mutableListOf<User>()
    var it: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener(this)

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()

        val userAdapter = UserAdapter(this, users)

        lvMain.adapter = userAdapter

        swiperefresh.setOnRefreshListener(this)

        lvMain.onItemClickListener = OnItemClickListener { _, view, _, id ->
            val intent = Intent(this, TryActivity::class.java)
            intent.putExtra("Handle", (view.findViewById<TextView>(R.id.tv1) as TextView).text)
            intent.putExtra("Id", it!![it!!.size - 1 - id.toInt()].id.toString())
            startActivity(intent)
        }

        val liveData = userDao.getAll()

        liveData.observe(this, Observer<List<User>> { t ->
            users.clear()
            it = t
            for (element in t!!.size - 1 downTo 0) {
                users.add(t[element])
                userAdapter.notifyDataSetChanged()
            }
            userAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        lateinit var userDao: UserDao
        const val ID = "Id"
    }

    private fun loadUser(handle: String) {
        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userApi = retrofit.create(UserApi::class.java)

        val user = userApi.user(handle)

        user.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                for ((counter, element) in response.body()!!.result.withIndex()) {
                    element.id = it!![counter].id
                    userDao.update(element)
                }
                progressBar.visibility = View.INVISIBLE
                swiperefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {}
        })
    }

    override fun onRefresh() {
        var handles = ""
        for (element in this.it!!) {
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