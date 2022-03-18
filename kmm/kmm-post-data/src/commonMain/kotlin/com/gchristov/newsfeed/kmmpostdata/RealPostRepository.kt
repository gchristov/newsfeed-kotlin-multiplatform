package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.model.calculateReadingTime
import com.gchristov.newsfeed.kmmpostdata.model.toPost
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

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
    ): DecoratedPost =
        withContext(dispatcher) {
            val post = apiService.post(postId, postMetadataFields).toPost()
            val decoratedPost = decoratePost(post)
            cachePost(decoratedPost)
            decoratedPost
        }

    override suspend fun redecoratePost(post: DecoratedPost): DecoratedPost =
        withContext(dispatcher) {
            decoratePost(post.raw)
        }

    private suspend fun decoratePost(post: Post) = DecoratedPost(
        raw = post,
        date = Instant.parse(post.date),
        favouriteTimestamp = favouriteTimestamp(post.id),
        readingTimeMinutes = post.calculateReadingTime()
    )

    override suspend fun cachedPost(postId: String): DecoratedPost? =
        withContext(dispatcher) {
            val post = queries.selectWithId(postId).executeAsOneOrNull() ?: return@withContext null
            decoratePost(post)
        }

    override suspend fun clearCache(postId: String) =
        withContext(dispatcher) {
            queries.transaction {
                queries.clearTable(postId)
            }
        }

    private fun cachePost(decoratedPost: DecoratedPost) {
        val post = decoratedPost.raw
        queries.insert(
            id = post.id,
            date = post.date,
            headline = post.headline,
            body = post.body,
            thumbnail = post.thumbnail,
        )
    }

    override suspend fun favouriteTimestamp(postId: String): Long? =
        withContext(dispatcher) {
            if (sharedPreferences.contains(postId)) {
                return@withContext sharedPreferences.getLong(
                    key = postId,
                    defaultValue = Clock.System.now().toEpochMilliseconds()
                )
            }
            null
        }

    override suspend fun toggleFavourite(postId: String) =
        withContext(dispatcher) {
            favouriteTimestamp(postId)?.let {
                sharedPreferences.remove(postId)
            } ?: run {
                // Keep track of when the item was favourited
                sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            }
        }
}