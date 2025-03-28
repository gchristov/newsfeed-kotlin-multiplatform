package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.model.toPost
import com.gchristov.newsfeed.multiplatform.post.data.usecase.EstimateReadingTimeMinutesUseCase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface PostRepository {
    suspend fun post(
        postId: String,
        postMetadataFields: String = "main,body,headline,thumbnail"
    ): Either<Throwable, DecoratedPost>

    suspend fun cachedPost(postId: String): Either<Throwable, DecoratedPost?>

    suspend fun clearCache(postId: String): Either<Throwable, Unit>

    suspend fun cachePost(decoratedPost: DecoratedPost): Either<Throwable, Unit>

    suspend fun favouriteTimestamp(postId: String): Either<Throwable, Long?>

    suspend fun toggleFavourite(postId: String): Either<Throwable, Unit>
}

internal class RealPostRepository(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: PostApi,
    private val sharedPreferences: Settings,
    private val estimateReadingTimeMinutesUseCase: EstimateReadingTimeMinutesUseCase,
    database: PostSqlDelightDatabase,
    private val analytics: FirebaseAnalytics
) : PostRepository {
    private val queries = database.postSqlDelightDatabaseQueries

    override suspend fun post(
        postId: String,
        postMetadataFields: String
    ): Either<Throwable, DecoratedPost> = withContext(dispatcher) {
        either {
            val postRsp = apiService.post(
                postUrl = postId,
                postMetadataFields = postMetadataFields,
            ).bind()
            val post = decoratePost(postRsp.toPost()).bind()
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
                readingTimeMinutes = estimateReadingTimeMinutes(post).bind(),
            )
        }
    }

    override suspend fun cachedPost(
        postId: String
    ): Either<Throwable, DecoratedPost?> = withContext(dispatcher) {
        val cachedPost = queries.selectPostWithId(postId).executeAsOneOrNull()
        cachedPost?.let {
            decoratePost(cachedPost)
        } ?: Either.Right(null)
    }

    override suspend fun clearCache(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        queries.transaction {
            queries.deletePost(postId)
        }
        Either.Right(Unit)
    }

    override suspend fun cachePost(
        decoratedPost: DecoratedPost
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        val post = decoratedPost.raw
        queries.insertPost(
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
        } else {
            Either.Right(null)
        }
    }

    override suspend fun toggleFavourite(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        analytics.logEvent("toggle-favourite", mapOf("id" to postId))

        either {
            val timestamp = favouriteTimestamp(postId).bind()
            if (timestamp != null) {
                sharedPreferences.remove(postId)
            } else {
                // Keep track of when the item was favourited
                sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            }
            Either.Right(Unit)
        }
    }

    private suspend fun estimateReadingTimeMinutes(
        post: Post
    ): Either<Throwable, Int> = withContext(dispatcher) {
        val allText = post.headline + " " + post.body
        estimateReadingTimeMinutesUseCase(EstimateReadingTimeMinutesUseCase.Dto(allText))
    }
}