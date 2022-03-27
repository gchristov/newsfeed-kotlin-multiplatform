package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost

interface PostRepository {
    suspend fun post(
        postId: String,
        postMetadataFields: String = "main,body,headline,thumbnail"
    ): Post

    //TODO: It should return the decorated one?
    // but SqlDelight has the raw post only and decoration
    // has been moved to usecase
    suspend fun cachedPost(postId: String): Post?

    suspend fun clearCache(postId: String)

    suspend fun cachePost(decoratedPost: DecoratedPost)

    suspend fun favouriteTimestamp(postId: String): Long?

    suspend fun toggleFavourite(postId: String)

}