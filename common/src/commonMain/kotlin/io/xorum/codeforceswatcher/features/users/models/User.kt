package io.xorum.codeforceswatcher.features.users.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import io.xorum.codeforceswatcher.DbUser

data class User(
        var id: Long = 0,
        val avatar: String,
        val rank: String?,
        val handle: String,
        val rating: Int?,
        val maxRating: Int?,
        val firstName: String?,
        val lastName: String?,
        var ratingChanges: List<RatingChange>
) {
    companion object {
        fun fromDB(dbUser: DbUser): User {
            val serializer = Json(JsonConfiguration.Stable.copy(strictMode = false))
            return User(
                    id = dbUser.id,
                    avatar = dbUser.avatar,
                    rank = dbUser.rank,
                    handle = dbUser.handle,
                    rating = dbUser.rating?.toInt(),
                    maxRating = dbUser.maxRating?.toInt(),
                    firstName = dbUser.firstName,
                    lastName = dbUser.lastName,
                    ratingChanges = serializer.parse(RatingChange.serializer().list, dbUser.ratingChanges)
            )
        }
    }
}
