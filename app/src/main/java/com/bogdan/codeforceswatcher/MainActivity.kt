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
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    private fun loadUser(handle: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.codeforces.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userApi = retrofit.create(UserApi::class.java)

        val user = userApi.user(etHandle.text.toString())

        user.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    names.add(0, handle)
                    adapter.notifyDataSetChanged()
                    saveText()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showError()
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnShow -> {
                loadUser(etHandle.text.toString())
                etHandle.text = null
            }
            else -> {
            }
        }
    }

    fun showError() {
        Toast.makeText(applicationContext, "Wrong handle!", Toast.LENGTH_SHORT).show()
    }

}
