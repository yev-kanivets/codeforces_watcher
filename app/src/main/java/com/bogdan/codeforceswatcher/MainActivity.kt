package com.bogdan.codeforceswatcher

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnClickListener {

    private val users = mutableListOf<User>()
    var it: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()

        btnShow.setOnClickListener(this)

        val userAdapter = UserAdapter(this, users)

        lvMain.adapter = userAdapter

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
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userApi = retrofit.create(UserApi::class.java)

        val user = userApi.user(handle)

        user.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    userDao.insert(response.body()!!.result.firstOrNull()!!)
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showError()
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                loadUser(etHandle.text.toString())
                etHandle.text = null
            }
            else -> {
            }
        }
    }

    fun showError() {
        Toast.makeText(applicationContext, "Wrong handle!", Toast.LENGTH_SHORT).show()
    }

}