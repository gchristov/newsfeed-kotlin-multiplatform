package com.gchristov.newsfeed.multiplatform.post.testfixtures

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.common.test.execute
import com.gchristov.newsfeed.multiplatform.post.data.Post
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class FakePostRepository(
    val post: Post? = null,
    val usePostForCache: Boolean = false,
    val readingTimeMinutes: Int? = null,
) : PostRepository {
    var postResponse: FakeResponse = FakeResponse.CompletesNormally

    private var favouritePosts = mutableMapOf<String, Long>()

    override suspend fun post(
        postId: String,
        postMetadataFields: String
    ): Either<Throwable, DecoratedPost> = either {
        val decoratedPost = DecoratedPost(
            raw = requireNotNull(post),
            date = Instant.parse(requireNotNull(post.date)),
            favouriteTimestamp = favouriteTimestamp(postId).bind(),
            readingTimeMinutes = readingTimeMinutes,
        )
        return try {
            val response = postResponse.execute(decoratedPost)
            Either.Right(response)
        } catch (error: Throwable) {
            Either.Left(error)
        }
    }

    override suspend fun cachedPost(postId: String): Either<Throwable, DecoratedPost?> = either {
        val decoratedPost = post?.takeIf { usePostForCache }?.let {
            DecoratedPost(
                raw = it,
                date = Instant.parse(requireNotNull(it.date)),
                favouriteTimestamp = favouriteTimestamp(postId).bind(),
                readingTimeMinutes = readingTimeMinutes,
            )
        }
        return Either.Right(decoratedPost)
    }

    override suspend fun clearCache(postId: String): Either<Throwable, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun cachePost(decoratedPost: DecoratedPost): Either<Throwable, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun favouriteTimestamp(postId: String): Either<Throwable, Long?> {
        return Either.Right(favouritePosts[postId])
    }

    override suspend fun toggleFavourite(postId: String): Either<Throwable, Unit> = either {
        val timestamp = favouriteTimestamp(postId).bind()
        if (timestamp != null) {
            favouritePosts.remove(postId)
        } else {
            // Keep track of when the item was favourited
            favouritePosts[postId] = Clock.System.now().toEpochMilliseconds()
        }
        Either.Right(Unit)
    }
}