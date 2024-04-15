package com.gchristov.newsfeed.multiplatform.feed.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage

interface FeedRepository {
    suspend fun feedPage(
        pageId: Int,
        feedQuery: String,
    ): DecoratedFeedPage

    suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): DecoratedFeedPage

    suspend fun cachedFeedPage(): Either<Throwable, DecoratedFeedPage?>

    suspend fun clearCache()

    suspend fun saveSearchQuery(searchQuery: String)

    suspend fun searchQuery(): String?
}