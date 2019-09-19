package com.bogdan.codeforceswatcher.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersResponse
import com.bogdan.codeforceswatcher.network.RestClient
import com.bogdan.codeforceswatcher.network.model.RatingChangeResponse
import com.bogdan.codeforceswatcher.room.DatabaseClient
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.activity_add_user.btnAdd
import kotlinx.android.synthetic.main.activity_add_user.etHandle
import kotlinx.android.synthetic.main.activity_add_user.progressBar
import kotlinx.android.synthetic.main.activity_add_user.toolbar
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

        val userCall = RestClient.getUsers(handle)
        val ratingCall = RestClient.getRating(handle)

        userCall.enqueue(object : Callback<UsersResponse> {

            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.result.firstOrNull() == null) {
                        progressBar.visibility = View.INVISIBLE
                        showError(getString(R.string.wrong))
                    } else {
                        val localUser = response.body()!!.result.firstOrNull()!!
                        ratingCall.enqueue(object : Callback<RatingChangeResponse> {

                            override fun onResponse(
                                call: Call<RatingChangeResponse>,
                                response: Response<RatingChangeResponse>
                            ) {
                                if (response.isSuccessful) {
                                    localUser.ratingChanges = response.body()!!.result
                                    val findUser = DatabaseClient.userDao.getAll()
                                        .find { it.handle == localUser.handle }
                                    if (findUser == null) {
                                        DatabaseClient.userDao.insert(localUser)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            getString(R.string.user_already_added),
                                            Toast.LENGTH_SHORT
                                        ).show()
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

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
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
                Analytics.logUserAdded()
            }
            else -> {
            }
        }
    }
}
