package com.bogdan.codeforceswatcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var adapter: ArrayAdapter<String>
    private val names = mutableListOf<String>()
    private var savedText = "Text"

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
        const val HANDLES = "Handle"
    }

    private fun loadText() {
        var s = ""
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedText = sPref.getString(this.savedText, "")
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
        ed.putString(savedText, etHandle.text.toString() + " " + sPref.getString(savedText, ""))
        ed.apply()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                names.add(0, etHandle.text.toString())
                adapter.notifyDataSetChanged()
                saveText()
                etHandle.text = null
            }
            else -> {
            }
        }
    }
}
