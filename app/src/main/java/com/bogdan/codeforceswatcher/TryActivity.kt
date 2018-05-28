package com.bogdan.codeforceswatcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_try.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        loadUser()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val TAG = "MyLog.s"
    }

    private fun loadUser() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userApi = retrofit.create(UserApi::class.java)

        val user = userApi.user(intent.getStringExtra(MainActivity.HANDLES))



        user.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "response " + response.body()!!)
                    displayUser(response.body()!!.result.firstOrNull()!!)
                } else {
                    Log.d(TAG, "response code " + response.code())
                    showError()
                    finish()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showError()
                Log.d(TAG, "failure $t")
                finish()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun displayUser(user: User) {
        tvRank.text = "Rank: " + user.rank
        tvCurrentRating.text = "CurrentRating: " + user.rating.toString()
        tvHandle.text = "Handle: " + user.handle
        tvMaxRating.text = "MaxRating: " + user.maxRating.toString()
        if(user.avatar.substring(0,6) != "https:"){
            Picasso.get().load("https:" + user.avatar).into(ivAvatar)
        }
        else {
            Picasso.get().load(user.avatar).into(ivAvatar)
        }
        title = user.firstName + " " + user.lastName
    }

    fun showError() {
        Toast.makeText(applicationContext, "No such handle found", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_try, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}