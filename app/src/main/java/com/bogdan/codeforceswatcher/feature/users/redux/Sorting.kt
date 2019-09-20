package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.room.DatabaseClient

object Sorting {

    fun sort(users: List<User>, sort: UsersState.Sort): List<User> =
        when (sort) {
            UsersState.Sort.DEFAULT -> DatabaseClient.userDao.getAll().reversed()
            UsersState.Sort.RATING_DOWN -> users.sortedByDescending(User::rating)
            UsersState.Sort.RATING_UP -> users.sortedBy(User::rating)
            UsersState.Sort.UPDATE_DOWN -> users.sortedByDescending { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            }
            UsersState.Sort.UPDATE_UP -> users.sortedBy { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            }
        }

}