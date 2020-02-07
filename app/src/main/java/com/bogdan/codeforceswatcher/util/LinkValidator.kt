package com.bogdan.codeforceswatcher.util

object LinkValidator {

    fun avatar(avatarLink: String) =
            if (avatarLink.startsWith("https:")) {
                avatarLink
            } else {
                "https:$avatarLink"
            }
}