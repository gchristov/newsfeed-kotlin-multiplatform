package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import arrow.core.flatMap
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.model.toPost
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

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
    ): Post = apiService.post(postId, postMetadataFields).body<ApiPostResponse>().toPost()

    override suspend fun cachedPost(postId: String): Post? =
        withContext(dispatcher) {
            val post = queries.selectWithId(postId).executeAsOneOrNull() ?: return@withContext null
            post
        }

    override suspend fun clearCache(postId: String) =
        withContext(dispatcher) {
            queries.transaction {
                queries.clearTable(postId)
            }
        }

    override suspend fun cachePost(decoratedPost: DecoratedPost) {
        val post = decoratedPost.raw
        queries.insert(
            id = post.id,
            date = post.date,
            headline = post.headline,
            body = post.body,
            thumbnail = post.thumbnail,
        )
    }

    override suspend fun favouriteTimestamp(postId: String): Either<Throwable, Long?> =
        withContext(dispatcher) {
            if (sharedPreferences.contains(postId)) {
                val timestamp = sharedPreferences.getLong(
                    key = postId,
                    defaultValue = Clock.System.now().toEpochMilliseconds()
                )
                Either.Right(timestamp)
            }
            Either.Right(null)
        }

    override suspend fun toggleFavourite(postId: String): Either<Throwable, Unit> =
        withContext(dispatcher) {
            favouriteTimestamp(postId).flatMap { timestamp ->
                timestamp?.let {
                    sharedPreferences.remove(postId)
                } ?: {
                    // Keep track of when the item was favourited
                    sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
                }
                Either.Right(Unit)
            }
        }
}