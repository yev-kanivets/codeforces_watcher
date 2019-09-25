package com.bogdan.codeforceswatcher.feature.users.redux

import com.bogdan.codeforceswatcher.model.User

fun List<User>.sort(sortType: UsersState.SortType) = when (sortType) {
    UsersState.SortType.DEFAULT -> reversed()
    UsersState.SortType.RATING_DOWN -> sortedByDescending(User::rating)
    UsersState.SortType.RATING_UP -> sortedBy(User::rating)
    UsersState.SortType.UPDATE_DOWN -> sortedByDescending { user ->
        user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
    }
    UsersState.SortType.UPDATE_UP -> sortedBy { user ->
        user.ratingChanges.lastOrNull()?.ratingUpdateTimeSeconds
    }
}