package com.gchristov.newsfeed.kmmfeeddata.usecase

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DecoratePostUseCase(
    private val postRepository: PostRepository,
    private val sharedPreferences: Settings,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun decoratedPost(
        postId: String
    ): DecoratedPost {

        return postRepository.run {
            cachedPost(postId)?.let { post ->
                clearCache(postId)
                decoratePost(post)
            } ?: queryNewPost(postId)
        }
    }

    suspend fun redecoratePost(post: DecoratedPost): DecoratedPost =
        withContext(dispatcher) {
            decoratePost(post.raw)
        }

    suspend fun toggleFavourite(postId: String) =
        withContext(dispatcher) {
            favouriteTimestamp(postId)?.let {
                sharedPreferences.remove(postId)
            } ?: run {
                // Keep track of when the item was favourited
                sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            }
        }

    private suspend fun queryNewPost(postId: String): DecoratedPost =
        postRepository.run {
            post(postId).let {
                val decoratedPost = decoratePost(it)
                cachePost(decoratedPost)
                decoratedPost
            }
        }

    private suspend fun decoratePost(post: Post) = DecoratedPost(
        raw = post,
        date = Instant.parse(post.date),
        favouriteTimestamp = favouriteTimestamp(post.id),
        readingTimeMinutes = calculateReadingTimeMinutes(post)
    )

    private suspend fun calculateReadingTimeMinutes(post: Post): Int =
        withContext(dispatcher) {
            val bodyWordCount = post.body?.split(" ")?.count() ?: 0
            val headerWordCount = post.headline?.split(" ")?.count() ?: 0
            val wordCount = bodyWordCount + headerWordCount
            ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
        }

    private suspend fun favouriteTimestamp(postId: String): Long? =
        withContext(dispatcher) {
            if (sharedPreferences.contains(postId)) {
                return@withContext sharedPreferences.getLong(
                    key = postId,
                    defaultValue = Clock.System.now().toEpochMilliseconds()
                )
            }
            null
        }
}
