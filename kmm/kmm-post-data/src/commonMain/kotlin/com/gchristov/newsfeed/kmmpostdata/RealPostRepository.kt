package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.model.toPost
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
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

        // Option 1: similar to `favouriteTimestamp` do it here in the repository
        // it is pragmatic, but it does not 'really' belong here
        // (favouriteTimestamp is, as it's directly using cache/sharedPrefs)

        // With current setup I cannot see a way of not doing this here.
        // One option could be moving this decoration logic into an UseCase
        // which would receive this repository and cache facilities via DI.
        // In there, do the calculation and rebuild the decoratedPost.
        // I see that you already using this Repository directly in the PostViewModel
        // so it would there but wrapped in a UseCase
        // (at least in DDD application-level it would be here)
        readingTimeMinutes = calculateReadingTimeMinutes(post)
    )

    private suspend fun decoratePost2(post: Post) = DecoratedPost(
        raw = post,
        date = Instant.parse(post.date),
        favouriteTimestamp = favouriteTimestamp(post.id),

        // Option 2: not doing it here in the Repository, as it is something
        // that belongs on decoration time use case
        // readingTimeMinutes = calculateReadingTimeMinutes(post)
        // see the follow-up comment in PostViewModel.kt
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

    override suspend fun calculateReadingTimeMinutes(post: Post): Int =
        withContext(dispatcher) {
            val bodyWordCount = post.body?.split(" ")?.count() ?: 0
            val headerWordCount = post.headline?.split(" ")?.count() ?: 0
            val wordCount = bodyWordCount + headerWordCount
            ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
        }
}