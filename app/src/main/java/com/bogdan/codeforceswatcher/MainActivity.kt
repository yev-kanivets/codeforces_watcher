package com.bogdan.codeforceswatcher

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.*
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room

class MainActivity : AppCompatActivity(), OnClickListener {

    val data = mutableListOf<Map<String, Any>>()
    val from = arrayOf(ATTRIBUTE_NAME_HANDLE, ATTRIBUTE_NAME_RATING)
    val to = intArrayOf(R.id.tv1, R.id.tv2)
    lateinit var sAdapter: SimpleAdapter
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "database").allowMainThreadQueries().build()

        userDao = db.userDao()

        btnShow.setOnClickListener(this)

        sAdapter = SimpleAdapter(this, data, R.layout.list_view, from, to)

        lvMain.adapter = sAdapter

        lvMain.onItemClickListener = OnItemClickListener { _, view, _, _ ->
            val intent = Intent(this, TryActivity::class.java)
            intent.putExtra("Handle", (view.findViewById<TextView>(R.id.tv1) as TextView).text)
            startActivity(intent)
        }

        val liveData = userDao.getAll()

        liveData.observe(this, Observer<List<User>> { t ->
            data.clear()
            for (element in t!!.size - 1 downTo 0) {
                val m = HashMap<String, Any>()
                m[ATTRIBUTE_NAME_HANDLE] = t[element].handle
                m[ATTRIBUTE_NAME_RATING] = t[element].rating
                data.add(m)
                sAdapter.notifyDataSetChanged()
            }
        })
    }

    companion object {
        const val HANDLES = "Handle"
        const val ATTRIBUTE_NAME_HANDLE = "handle"
        const val ATTRIBUTE_NAME_RATING = "rating"
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

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Long): User

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

}

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}