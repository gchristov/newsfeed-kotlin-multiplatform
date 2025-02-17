package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost

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