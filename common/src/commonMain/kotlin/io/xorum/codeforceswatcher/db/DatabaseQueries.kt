package io.xorum.codeforceswatcher.db

import io.xorum.codeforceswatcher.features.contests.models.Contest
import io.xorum.codeforceswatcher.features.problems.models.Problem
import io.xorum.codeforceswatcher.features.users.models.ListRatingChanges
import io.xorum.codeforceswatcher.features.users.models.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

object DatabaseQueries {
    object Users {
        fun getAll() = database.userQueries.getAll().executeAsList().map { User.fromDB(it) }
        fun insert(user: User): Long {
            val serializer = Json(JsonConfiguration.Stable.copy(strictMode = false))
            val ratingChanges = serializer.stringify(ListRatingChanges.serializer(), ListRatingChanges(user.ratingChanges))

            if (user.id != 0L) {
                database.userQueries.update(user.id, user.avatar, user.rank, user.handle, user.rating?.toLong(), user.maxRating?.toLong(), user.firstName, user.lastName, ratingChanges)
            } else {
                database.userQueries.insert(user.avatar, user.rank, user.handle, user.rating?.toLong(), user.maxRating?.toLong(), user.firstName, user.lastName, ratingChanges)
            }
            return database.userQueries.getIndex().executeAsOne()
        }

        fun delete(userId: Long) = database.userQueries.delete(userId)
    }

    object Contests {
        fun getAll() = database.contestQueries.getAll().executeAsList().map { Contest.fromDB(it) }

        fun insert(contests: List<Contest>) {
            database.contestQueries.transaction {
                contests.forEach { contest ->
                    database.contestQueries.insert(contest.id, contest.name, contest.startTimeSeconds, contest.durationSeconds, contest.phase)
                }
            }
        }

        fun deleteAll() = database.contestQueries.deleteAll()
    }

    object Problems {
        fun getAll() = database.problemQueries.getAll().executeAsList().map { Problem.fromDB(it) }

        fun insert(problems: List<Problem>): List<Long> {
            database.problemQueries.transaction {
                for (problem in problems) {
                    insert(problem)
                }
            }

            val databaseProblems = getAll()
            val identifiers = hashMapOf<String, Long>()
            databaseProblems.forEach { identifiers[it.identify()] = it.id }
            val resultIds = mutableListOf<Long>()
            problems.forEach { identifiers[it.identify()]?.let { id -> resultIds.add(id) } }

            return resultIds
        }

        fun insert(problem: Problem) = if (problem.id != 0L) {
            database.problemQueries.update(problem.id, problem.name, problem.enName, problem.ruName, problem.index, problem.contestId, problem.contestName, problem.contestTime, problem.isFavourite)
        } else {
            database.problemQueries.insert(problem.name, problem.enName, problem.ruName, problem.index, problem.contestId, problem.contestName, problem.contestTime, problem.isFavourite)
        }

        fun deleteAll() = database.problemQueries.deleteAll()
    }
}