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
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    private fun loadUser() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userApi = retrofit.create(UserApi::class.java)

        val user = userApi.user(etHandle.text.toString())

        user.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    names.add(0, etHandle.text.toString())
                    Log.d("MY_TAG", etHandle.text.toString())
                    adapter.notifyDataSetChanged()
                    saveText()
                    etHandle.text = null
                } else {
                    showError()
                    etHandle.text = null
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showError()
                etHandle.text = null
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                loadUser()
            }
            else -> {
            }
        }
    }

    fun showError() {
        Toast.makeText(applicationContext, "Wrong handle!", Toast.LENGTH_SHORT).show()
    }

}
