package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage

interface FeedRepository {
    suspend fun feedPage(
        pageId: Int,
        feedQuery: String,
    ): DecoratedFeedPage

    suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): DecoratedFeedPage

    suspend fun cachedFeedPage(): DecoratedFeedPage?

    suspend fun clearCache()

    suspend fun saveSearchQuery(searchQuery: String)

    suspend fun searchQuery(): String?
}