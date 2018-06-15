package com.bogdan.codeforceswatcher.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_try.*

class TryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        displayUser(CwApp.app.userDao.getById(intent.getStringExtra(MainActivity.ID).toLong()))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun displayUser(user: User) {
        if (user.rank == null) {
            tvRank.text = getString(R.string.rank, getString(R.string.norank))
        } else {
            tvRank.text = getString(R.string.rank, user.rank)
        }
        if (user.rating == null) {
            tvCurrentRating.text = getString(R.string.currating, getString(R.string.nocurrating))
        } else {
            tvCurrentRating.text = getString(R.string.currating, user.rating.toString())
        }
        val handle = if (user.firstName == null && user.lastName == null) {
            getString(R.string.noname)
        } else if (user.firstName == null) {
            user.lastName
        } else if (user.lastName == null) {
            user.firstName
        } else
            user.firstName + " " + user.lastName
        tvHandle.text = getString(R.string.name, handle)
        if (user.maxRating == null) {
            tvMaxRating.text = getString(R.string.maxrating, getString(R.string.nomaxrating))
        } else {
            tvMaxRating.text = getString(R.string.maxrating, user.maxRating.toString())
        }
        if (user.avatar.substring(0, 6) != "https:") {
            Picasso.get().load("https:" + user.avatar).into(ivAvatar)
        } else {
            Picasso.get().load(user.avatar).into(ivAvatar)
        }
        title = user.handle
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_try, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                val id = (intent.getStringExtra(MainActivity.ID)).toLong()
                CwApp.app.userDao.delete(CwApp.app.userDao.getById(id))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}