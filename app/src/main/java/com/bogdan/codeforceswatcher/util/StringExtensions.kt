package com.bogdan.codeforceswatcher.util

import androidx.core.text.HtmlCompat

fun String.convertFromHtml(): String {
    val normalizedText = this.replace("\n", "<br>")
            .replace("\t", "<tl>")
            .replace("$", "")

    return HtmlCompat.fromHtml(normalizedText, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toString()
}
fun String.splitStringInHalf(): Pair<String, String> {
    val words = this.split(" ")
    val halfLength = words.size / 2
    val firstHalf = words.subList(0, halfLength).joinToString(" ")
    val secondHalf = words.subList(halfLength, words.size).joinToString(" ")

    return Pair(firstHalf, secondHalf)
}