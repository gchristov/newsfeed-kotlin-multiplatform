package com.gchristov.newsfeed.multiplatform.post.data.usecase

import com.gchristov.newsfeed.multiplatform.post.data.Post
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.util.ReadingTimeCalculator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

interface DecoratePostUseCase {
    suspend operator fun invoke(
        postId: String,
        onCache: ((DecoratedPost) -> Unit)? = null,
    ): DecoratedPost
}

class RealDecoratePostUseCase(
    private val postRepository: PostRepository,
    private val dispatcher: CoroutineDispatcher
) : DecoratePostUseCase {
    /**
     * Obtain a new post from API/repository, decorate and
     * cache it.
     * Optionally provide a callback that eagerly retrieves/checks
     * the posts cache
     *
     * @param postId the ID of the post to obtain
     * @param onCache callback that pulls posts from cache if existing
     */
    override suspend fun invoke(
        postId: String,
        onCache: ((DecoratedPost) -> Unit)?
    ): DecoratedPost {
        // We wouldn't even run this if the caller doesn't care about cache
        onCache?.let { cacheCallback ->
            cachedPost(postId)?.let { post ->
                // this is normally to update the UI from cache while
                // a new one loads
                cacheCallback(post)
            }
        }
        return fetchDecoratedPost(postId)
    }

    /**
     * This method is private to force callers to either handle cache or not explicitly
     * via callback
     */
    private suspend fun cachedPost(postId: String): DecoratedPost? =
        postRepository.run {
            cachedPost(postId)?.let { post ->
                decoratePost(post)
            }
        }

    private suspend fun fetchDecoratedPost(postId: String): DecoratedPost =
        postRepository.run {
            post(postId).let {
                clearCache(postId)
                val decoratedPost = decoratePost(it)
                cachePost(decoratedPost)
                decoratedPost
            }
        }

    private suspend fun decoratePost(post: Post) = DecoratedPost(
        raw = post,
        date = Instant.parse(post.date),
        // TODO: Fix this
//        favouriteTimestamp = postRepository.favouriteTimestamp(post.id),
        readingTimeMinutes = calculateReadingTimeMinutes(post)
    )

    private suspend fun calculateReadingTimeMinutes(post: Post): Int =
        withContext(dispatcher) {
            val bodyWordCount = post.body?.split(" ")?.count() ?: 0
            val headerWordCount = post.headline?.split(" ")?.count() ?: 0
            val wordCount = bodyWordCount + headerWordCount
            ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
        }
}
