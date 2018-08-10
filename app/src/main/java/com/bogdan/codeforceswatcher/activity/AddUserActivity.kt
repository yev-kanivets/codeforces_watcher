package com.bogdan.codeforceswatcher.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.model.RatingChangeResponse
import com.bogdan.codeforceswatcher.model.UserResponse
import kotlinx.android.synthetic.main.activity_add_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        btnShow.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadUser(handle: String) {
        progressBar.visibility = View.VISIBLE

        val userCall = CwApp.app.userApi.user(handle)

        val ratingCall = CwApp.app.userApi.rating(handle)

        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.result.firstOrNull() == null) {
                        progressBar.visibility = View.INVISIBLE
                        showError()
                    } else {
                        val localUser = response.body()!!.result.firstOrNull()!!
                        ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                            override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                                progressBar.visibility = View.INVISIBLE
                                if (response.isSuccessful) {
                                    localUser.ratingChanges = response.body()!!.result
                                    val findUser = CwApp.app.userDao.getAll().find { it.handle == localUser.handle }
                                    if (findUser == null) {
                                        CwApp.app.userDao.insert(localUser)
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.user_already_added), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {
                                progressBar.visibility = View.INVISIBLE
                            }
                        })
                    }
                } else {
                    progressBar.visibility = View.INVISIBLE
                    showError()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                CwApp.app.showError()
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
        Toast.makeText(applicationContext, getString(R.string.wrong), Toast.LENGTH_SHORT).show()
    }

}
