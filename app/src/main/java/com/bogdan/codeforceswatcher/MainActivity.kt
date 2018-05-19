package com.bogdan.codeforceswatcher

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), OnClickListener {

    val data = mutableListOf<Map<String, Any>>()
    val from = arrayOf(ATTRIBUTE_NAME_HANDLE, ATTRIBUTE_NAME_RATING)
    val to = intArrayOf(R.id.tv1, R.id.tv2)
    lateinit var sAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShow.setOnClickListener(this)

        sAdapter = SimpleAdapter(this, data, R.layout.list_view, from, to)

        loadText()

        lvMain.adapter = sAdapter

        lvMain.onItemClickListener = OnItemClickListener { _, view, _, _ ->
            val intent = Intent(this, TryActivity::class.java)
            intent.putExtra("Handle", (view.findViewById<TextView>(R.id.tv1) as TextView).text)
            startActivity(intent)
        }

    }

    companion object {
        const val HANDLES = "Handle"
        const val SAVED_TEXT = "Text"
        const val ATTRIBUTE_NAME_HANDLE = "handle"
        const val ATTRIBUTE_NAME_RATING = "rating"
    }

    private fun loadText() {
        var s = ""
        var k = 0
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val savedText = sPref.getString(SAVED_TEXT, "") + " "
        var symbol = 0
        while (symbol != savedText.length) {
            if (savedText[symbol] != ' ') {
                s += savedText[symbol]
            } else {
                if (!s.isEmpty()) {
                    val m = HashMap<String, Any>()
                    if (k % 2 == 0) {
                        m[ATTRIBUTE_NAME_HANDLE] = s
                        s = ""
                        for (i in symbol + 1 until savedText.length) {
                            if (savedText[i] != ' ') {
                                s += savedText[i]
                            } else {
                                m[ATTRIBUTE_NAME_RATING] = s
                                data.add(m)
                                Log.d(TryActivity.TAG + k.toString(), m.toString() + "\n")
                                Log.d(TryActivity.TAG, data.toString())
                                symbol = i
                                break
                            }
                        }
                    }
                    k += 2
                }
                s = ""
            }
            symbol++
        }
    }

    private fun saveText(handle: String, rating: String) {
        val sPref = getPreferences(Context.MODE_PRIVATE)
        val ed = sPref.edit()
        ed.putString(SAVED_TEXT, handle + " " + rating + " " + sPref.getString(SAVED_TEXT, ""))
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
                    val m = HashMap<String, Any>()
                    val rating = response.body()!!.result.firstOrNull()!!.rating.toString()
                    m[ATTRIBUTE_NAME_RATING] = rating
                    m[ATTRIBUTE_NAME_HANDLE] = handle
                    data.add(0, m)
                    Log.d(TryActivity.TAG, data.toString() + m.toString())
                    sAdapter.notifyDataSetChanged()
                    saveText(handle, rating)
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
