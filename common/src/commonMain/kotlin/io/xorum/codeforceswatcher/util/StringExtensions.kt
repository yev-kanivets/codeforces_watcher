package io.xorum.codeforceswatcher.util

fun String.defineLang() = if (this == "ru" || this == "uk") "ru" else "en"
