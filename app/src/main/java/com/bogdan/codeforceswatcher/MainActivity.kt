package com.bogdan.codeforceswatcher

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter



class MainActivity : AppCompatActivity(), OnClickListener {

    val names = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShow.setOnClickListener(this)

        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, names)

        lvMain.adapter = adapter
    }

    companion object {
        val HANDLES = "Handle"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                val intent = Intent(this, TryActivity::class.java)
                intent.putExtra("Handle", etHandle.text.toString())
                startActivity(intent)
                names.add(etHandle.text.toString())

            }
            else -> {
            }
        }
    }
}
