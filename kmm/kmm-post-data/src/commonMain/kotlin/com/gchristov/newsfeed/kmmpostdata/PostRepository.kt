package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost

interface PostRepository {
    suspend fun post(
        postId: String,
        postMetadataFields: String = "main,body,headline,thumbnail"
    ): DecoratedPost

    suspend fun redecoratePost(post: DecoratedPost): DecoratedPost

    suspend fun cachedPost(postId: String): DecoratedPost?

    suspend fun clearCache(postId: String)

    suspend fun favouriteTimestamp(postId: String): Long?

    suspend fun toggleFavourite(postId: String)

    suspend fun calculateReadingTimeMinutes(post: Post): Int
}