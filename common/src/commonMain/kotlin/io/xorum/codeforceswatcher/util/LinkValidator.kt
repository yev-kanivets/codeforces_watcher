package io.xorum.codeforceswatcher.util

fun avatar(avatarLink: String) = if (avatarLink.startsWith("https:")) {
    avatarLink
} else {
    "https:$avatarLink"
}
