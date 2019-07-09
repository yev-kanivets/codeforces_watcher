package com.bogdan.codeforceswatcher.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnAdd.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadUser(handle: String) {
        progressBar.visibility = View.VISIBLE

        val userCall = CwApp.app.codeforcesApi.getUsers(handle)

        val ratingCall = CwApp.app.codeforcesApi.getRating(handle)

        userCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.result.firstOrNull() == null) {
                        progressBar.visibility = View.INVISIBLE
                        showError(getString(R.string.wrong))
                    } else {
                        val localUser = response.body()!!.result.firstOrNull()!!
                        ratingCall.enqueue(object : Callback<RatingChangeResponse> {
                            override fun onResponse(call: Call<RatingChangeResponse>, response: Response<RatingChangeResponse>) {
                                if (response.isSuccessful) {
                                    localUser.ratingChanges = response.body()!!.result
                                    val findUser = CwApp.app.userDao.getAll().find { it.handle == localUser.handle }
                                    if (findUser == null) {
                                        CwApp.app.userDao.insert(localUser)
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, getString(R.string.user_already_added), Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    showError(getString(R.string.wrong))
                                }
                                progressBar.visibility = View.INVISIBLE
                            }

                            override fun onFailure(call: Call<RatingChangeResponse>, t: Throwable) {
                                progressBar.visibility = View.INVISIBLE
                                showError()
                            }
                        })
                    }
                } else {
                    progressBar.visibility = View.INVISIBLE
                    showError(getString(R.string.wrong))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                showError()
            }
        })
    }

    fun showError(message: String = getString(R.string.no_internet_connection)) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAdd -> {
                loadUser(etHandle.text.toString())
                etHandle.text = null
            }
            else -> {
            }
        }
    }

}
