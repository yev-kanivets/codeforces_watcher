package com.bogdan.codeforceswatcher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddUserActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        btnShow.setOnClickListener(this)

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
                    MainActivity.userDao.insert(response.body()!!.result.firstOrNull()!!)
                    finish()
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
