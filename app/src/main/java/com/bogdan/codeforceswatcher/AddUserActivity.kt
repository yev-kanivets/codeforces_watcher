package com.bogdan.codeforceswatcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        btnShow.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadUser(handle: String) {

        val userCall = CwApp.app.userApi.user(handle)

        val ratingCall = CwApp.app.userApi.rating(handle)

        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val localUser = response.body()!!.result.firstOrNull()!!
                    ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                        override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                            if (response.isSuccessful) {
                                localUser.ratingChanges = response.body()!!.result
                                CwApp.app.userDao.insert(localUser)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {}
                    })
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
