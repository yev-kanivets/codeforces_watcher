package com.bogdan.codeforceswatcher.util

object Validator {

    fun validateAvatarLink(avatarLink: String) =
        if (avatarLink.startsWith("https:")) {
            avatarLink
        } else {
            "https:$avatarLink"
        }
}