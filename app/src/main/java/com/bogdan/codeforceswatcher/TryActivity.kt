package com.bogdan.codeforceswatcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bogdan.codeforceswatcher.MainActivity.Companion.userDao
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_try.*

class TryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        displayUser(userDao.getById(intent.getStringExtra(MainActivity.ID).toLong()))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun displayUser(user: User) {
        tvRank.text = "Rank: " + user.rank
        tvCurrentRating.text = "CurrentRating: " + user.rating.toString()
        tvHandle.text = "Handle: " + user.handle
        tvMaxRating.text = "MaxRating: " + user.maxRating.toString()
        if (user.avatar.substring(0, 6) != "https:") {
            Picasso.get().load("https:" + user.avatar).into(ivAvatar)
        } else {
            Picasso.get().load(user.avatar).into(ivAvatar)
        }
        title = if (user.firstName == null && user.lastName == null) {
            "No Name"
        } else if (user.firstName == null) {
            user.lastName
        } else if (user.lastName == null) {
            user.firstName
        } else
            user.firstName + " " + user.lastName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_try, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                val id = (intent.getStringExtra(MainActivity.ID)).toLong()
                userDao.delete(userDao.getById(id))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}