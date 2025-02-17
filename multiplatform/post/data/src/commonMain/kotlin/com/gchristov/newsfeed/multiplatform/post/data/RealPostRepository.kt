package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.model.toPost
import com.gchristov.newsfeed.multiplatform.post.data.util.ReadingTimeCalculator
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.truncate

internal class RealPostRepository(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: PostApi,
    private val sharedPreferences: Settings,
    database: PostSqlDelightDatabase
) : PostRepository {
    private val queries = database.postSqlDelightDatabaseQueries

    override suspend fun post(
        postId: String,
        postMetadataFields: String
    ): Either<Throwable, DecoratedPost> = withContext(dispatcher) {
        // TODO: Does this need to be wrapped with try catch?
        val postResponse = apiService.post(
            postUrl = postId,
            postMetadataFields = postMetadataFields,
        ).body<ApiPostResponse>()
        either {
            val post = decoratePost(postResponse.toPost()).bind()
            clearCache(post.raw.id).bind()
            cachePost(post)
            post
        }
    }

    private suspend fun decoratePost(
        post: Post,
    ): Either<Throwable, DecoratedPost> = withContext(dispatcher) {
        either {
            DecoratedPost(
                raw = post,
                date = Instant.parse(post.date),
                favouriteTimestamp = favouriteTimestamp(post.id).bind(),
                readingTimeMinutes = calculateReadingTimeMinutes(post).bind(),
            )
        }
    }

    override suspend fun cachedPost(
        postId: String
    ): Either<Throwable, DecoratedPost?> = withContext(dispatcher) {
        val post = queries.selectWithId(postId).executeAsOneOrNull()
        post?.let {
            decoratePost(post)
        } ?: Either.Right(null)
    }

    override suspend fun clearCache(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        queries.transaction {
            queries.clearTable(postId)
        }
        Either.Right(Unit)
    }

    override suspend fun cachePost(
        decoratedPost: DecoratedPost
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        val post = decoratedPost.raw
        queries.insert(
            id = post.id,
            date = post.date,
            headline = post.headline,
            body = post.body,
            thumbnail = post.thumbnail,
        )
        Either.Right(Unit)
    }

    override suspend fun favouriteTimestamp(
        postId: String
    ): Either<Throwable, Long?> = withContext(dispatcher) {
        if (sharedPreferences.contains(postId)) {
            val timestamp = sharedPreferences.getLong(
                key = postId,
                defaultValue = Clock.System.now().toEpochMilliseconds()
            )
            Either.Right(timestamp)
        }
        Either.Right(null)
    }

    override suspend fun toggleFavourite(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        either {
            val timestamp = favouriteTimestamp(postId).bind()
            timestamp?.let {
                sharedPreferences.remove(postId)
            } ?: {
                // Keep track of when the item was favourited
                sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            }
            Either.Right(Unit)
        }
    }

    // TODO: Take this out into a separate use-case
    private suspend fun calculateReadingTimeMinutes(post: Post): Either<Throwable, Int> =
        withContext(dispatcher) {
            val bodyWordCount = post.body?.split(" ")?.count() ?: 0
            val headerWordCount = post.headline?.split(" ")?.count() ?: 0
            val wordCount = bodyWordCount + headerWordCount
            calculateReadingTimeMinutes(wordCount)
        }

    private fun calculateReadingTimeMinutes(totalWordCount: Int): Either<Throwable, Int> {
        if (totalWordCount <= 0) {
            return Either.Right(0)
        }

        val minutesWithDecimals = totalWordCount / 200.toDouble()
        val fullMinutes = truncate(minutesWithDecimals).toInt()

        val decimalPart = minutesWithDecimals - fullMinutes
        val extraSeconds = (decimalPart * 0.60) * 100
        val extraMinutes = if (extraSeconds.toInt() > 30) 1 else 0
        val totalMinutes = fullMinutes + extraMinutes

        return Either.Right(if (totalMinutes == 0) 1 else totalMinutes)
    }
}