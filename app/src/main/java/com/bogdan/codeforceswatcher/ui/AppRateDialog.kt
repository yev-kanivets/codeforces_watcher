package com.bogdan.codeforceswatcher.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.util.Prefs
import kotlinx.android.synthetic.main.dialog_rate.btnMaybe
import kotlinx.android.synthetic.main.dialog_rate.btnNo
import kotlinx.android.synthetic.main.dialog_rate.btnYes

class AppRateDialog : DialogFragment(), View.OnClickListener {

    private val prefs = Prefs.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return View.inflate(activity, R.layout.dialog_rate, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMaybe.setOnClickListener(this)
        btnNo.setOnClickListener(this)
        btnYes.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnYes -> onClickYes()
            R.id.btnNo -> onClickNo()
        }
        dismiss()
    }

    private fun onClickYes() {
        try {
            val uri = Uri.parse(GP_MARKET + requireContext().packageName)
            requireContext().startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.google_play_not_found),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
            dismiss()
        }
        prefs.appRated()
    }

    private fun onClickNo() {
        if (prefs.ratePeriod == BIG_PERIOD) {
            prefs.appRated()
        } else {
            prefs.ratePeriod = BIG_PERIOD
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {

        private const val GP_MARKET = "market://details?id="
        private const val BIG_PERIOD = 25
    }
}
