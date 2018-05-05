package com.bogdan.codeforceswatcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import android.R.id.edit
import android.util.Log
import android.R.id.edit
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView


class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var adapter: ArrayAdapter<String>
    val names = mutableListOf<String>()
    var SAVED_TEXT = "Text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadText()

        btnShow.setOnClickListener(this)

        adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, names)

        lvMain.adapter = adapter

        lvMain.onItemClickListener = OnItemClickListener { _, view, _, _ ->
            val intent = Intent(this, TryActivity::class.java)
            intent.putExtra("Handle", (view as TextView).text)
            startActivity(intent)
        }

    }


    companion object {
        val HANDLES = "Handle"
    }

    private fun loadText() {
        var s = ""
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedText = sPref.getString(SAVED_TEXT, "")
        for (symbol in savedText) {
            if (symbol != ' ') {
                s += symbol
            } else {
                if (!s.isEmpty())
                    names.add(s)
                s = ""
            }
        }
        if (!s.isEmpty())
            names.add(s)
    }

    private fun saveText() {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putString(SAVED_TEXT, sPref.getString(SAVED_TEXT, "") + " " + etHandle.text.toString())
        ed.commit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                names.add(0, etHandle.text.toString())
                adapter.notifyDataSetChanged()
                lvMain.deferNotifyDataSetChanged()
                saveText()
                etHandle.text = null
            }
            else -> {
            }
        }
    }
}
