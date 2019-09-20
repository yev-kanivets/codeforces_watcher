package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.model.User
import com.bogdan.codeforceswatcher.room.DatabaseClient

object Sorting {

    fun sort(users: List<User>, sortType: UsersState.SortType): List<User> =
        when (sortType) {
            UsersState.SortType.DEFAULT -> DatabaseClient.userDao.getAll().reversed()
            UsersState.SortType.RATING_DOWN -> users.sortedByDescending(User::rating)
            UsersState.SortType.RATING_UP -> users.sortedBy(User::rating)
            UsersState.SortType.UPDATE_DOWN -> users.sortedByDescending { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            }
            UsersState.SortType.UPDATE_UP -> users.sortedBy { user ->
                user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
            }
        }

}