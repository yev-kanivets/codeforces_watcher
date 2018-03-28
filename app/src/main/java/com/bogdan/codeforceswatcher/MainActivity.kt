package com.bogdan.codeforceswatcher

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShow.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                val intent = Intent(this, TryActivity::class.java)
                intent.putExtra("Handle", etHandle.text.toString())
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}
