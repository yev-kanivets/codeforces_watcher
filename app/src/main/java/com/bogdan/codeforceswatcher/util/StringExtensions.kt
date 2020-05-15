package com.bogdan.codeforceswatcher.util

import androidx.core.text.HtmlCompat

fun String.convertFromHtml(): String {
    val normalizedText = this.replace("\n", "<br>")
            .replace("\t", "<tl>")
            .replace("$", "")

    return HtmlCompat.fromHtml(normalizedText, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toString()
}