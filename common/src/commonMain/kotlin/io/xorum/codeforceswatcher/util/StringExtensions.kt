package io.xorum.codeforceswatcher.util

fun String.defineLang() = if (this == "ru" || this == "uk") "ru" else "en"

fun String.splitStringInHalf(): Pair<String, String> {
    val words = this.split(" ")
    val halfLength = words.size / 2
    val firstHalf = words.subList(0, halfLength).joinToString(" ")
    val secondHalf = words.subList(halfLength, words.size).joinToString(" ")

    return Pair(firstHalf, secondHalf)
}