package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeeddata.model.Feed

interface FeedRepository {
    suspend fun feed(pageId: String? = null): Feed

    suspend fun clearCache()

    suspend fun post(postId: String): DecoratedPost

    fun favouriteTimestamp(postId: String): Long?

    fun toggleFavourite(postId: String): Long?
}