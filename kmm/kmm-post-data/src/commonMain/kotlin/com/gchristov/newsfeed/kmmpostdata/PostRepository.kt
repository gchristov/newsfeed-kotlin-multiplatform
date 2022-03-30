package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost

interface PostRepository {
    suspend fun post(
        postId: String,
        postMetadataFields: String = "main,body,headline,thumbnail"
    ): Post

    suspend fun cachedPost(postId: String): Post?

    suspend fun clearCache(postId: String)

    suspend fun cachePost(decoratedPost: DecoratedPost)

    suspend fun favouriteTimestamp(postId: String): Long?

    suspend fun toggleFavourite(postId: String)

}