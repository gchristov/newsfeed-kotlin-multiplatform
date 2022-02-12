package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedFeedPage

interface FeedRepository {
    suspend fun feedPage(
        pageId: Int,
        feedQuery: String,
    ): DecoratedFeedPage

    suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): DecoratedFeedPage

    suspend fun cachedFeedPage(): DecoratedFeedPage?

    suspend fun clearCache()
}