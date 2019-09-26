package com.bogdan.codeforceswatcher.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.util.Analytics
import kotlinx.android.synthetic.main.activity_add_user.btnAdd
import kotlinx.android.synthetic.main.activity_add_user.toolbar

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

    fun showError(message: String = getString(R.string.no_internet_connection)) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAdd -> {
                //loadUser(etHandle.text.toString())
                Analytics.logUserAdded()
            }
            else -> {
            }
        }
    }
}
