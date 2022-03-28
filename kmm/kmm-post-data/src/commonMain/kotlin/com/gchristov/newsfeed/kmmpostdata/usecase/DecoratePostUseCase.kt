package com.gchristov.newsfeed.kmmpostdata.usecase

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

class DecoratePostUseCase(
    private val postRepository: PostRepository,
    private val dispatcher: CoroutineDispatcher
) {

//    suspend fun decoratedPost(
//        postId: String
//    ): DecoratedPost {
//
//        return postRepository.run {
//            cachedPost(postId)?.let { post ->
//                clearCache(postId)
//                decoratePost(post)
//            } ?: queryNewPost(postId)
//        }
//    }

    suspend fun decoratedPost(
        postId: String
    ): DecoratedPost {

        return postRepository.run {
//            cachedPost(postId)?.let { post ->
//                clearCache(postId)
//                decoratePost(post)
//            } ?:
            queryNewPost(postId)
        }
    }

    suspend fun cachedPost(postId: String): DecoratedPost? =
        postRepository.cachedPost(postId)?.let { post ->
            decoratePost(post)
        }

    suspend fun clearCache(postId: String) = postRepository.clearCache(postId)

    suspend fun redecoratePost(post: DecoratedPost): DecoratedPost = decoratePost(post.raw)

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
        favouriteTimestamp = postRepository.favouriteTimestamp(post.id),
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
