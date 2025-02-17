package com.gchristov.newsfeed.multiplatform.post.testfixtures

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.common.test.execute
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import kotlinx.datetime.Clock

class FakePostRepository(
    val post: DecoratedPost? = null,
    val postCache: DecoratedPost? = null,
) : PostRepository {
    var postResponse: FakeResponse = FakeResponse.CompletesNormally

    private var _cacheCleared = false
    private var _favouritePosts = mutableMapOf<String, Long>().apply {
        post?.let { post ->
            post.favouriteTimestamp?.let { put(post.raw.id, it) }
        }
    }

    override suspend fun post(
        postId: String,
        postMetadataFields: String
    ): Either<Throwable, DecoratedPost> {
        return Either.Right(postResponse.execute(requireNotNull(post)))
    }

    override suspend fun cachedPost(postId: String): Either<Throwable, DecoratedPost?> {
        return Either.Right(postCache)
    }

    override suspend fun clearCache(postId: String): Either<Throwable, Unit> {
        _cacheCleared = true
        return Either.Right(Unit)
    }

    override suspend fun cachePost(decoratedPost: DecoratedPost): Either<Throwable, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun favouriteTimestamp(postId: String): Either<Throwable, Long?> {
        return Either.Right(_favouritePosts[postId])
    }

    override suspend fun toggleFavourite(postId: String): Either<Throwable, Unit> = either {
        favouriteTimestamp(postId).bind().let {
            _favouritePosts.remove(postId)
        } ?: run {
            // Keep track of when the item was favourited
            _favouritePosts[postId] = Clock.System.now().toEpochMilliseconds()
        }
    }

    fun assertCacheCleared() = _cacheCleared
}