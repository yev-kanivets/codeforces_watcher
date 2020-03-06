package io.xorum.codeforceswatcher.features.users.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

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
        fun fromDB(dbUser: io.xorum.codeforceswatcher.User): User {
            val ratingChanges = dbUser.ratingChanges
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
                    ratingChanges = serializer.parse(ListRatingChanges.serializer(), ratingChanges).ratingChanges
            )
        }
    }
}

@Serializable
data class ListRatingChanges(val ratingChanges: List<RatingChange>)