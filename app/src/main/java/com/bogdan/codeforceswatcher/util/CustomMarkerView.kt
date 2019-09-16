package com.bogdan.codeforceswatcher.util

import android.annotation.SuppressLint
import android.content.Context
import com.bogdan.codeforceswatcher.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.chart.view.tvContent

class CustomMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    override fun getOffset(): MPPointF {
        return MPPointF((-(width * 0.95)).toFloat(), (-height).toFloat())
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e?.data == null) {
            tvContent.text = context.getString(R.id.none)
        } else {
            tvContent.text = e.data.toString()
        }
        super.refreshContent(e, highlight)
    }
}
