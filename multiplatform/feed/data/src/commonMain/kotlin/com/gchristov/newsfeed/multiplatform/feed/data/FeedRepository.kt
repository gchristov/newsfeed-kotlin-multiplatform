package com.gchristov.newsfeed.multiplatform.feed.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage

interface FeedRepository {
    suspend fun feedPage(
        pageId: Int,
        feedQuery: String,
    ): Either<Throwable, DecoratedFeedPage>

    suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): Either<Throwable, DecoratedFeedPage>

    suspend fun cachedFeedPage(): Either<Throwable, DecoratedFeedPage?>

    suspend fun clearCache(): Either<Throwable, Unit>

    suspend fun saveSearchQuery(searchQuery: String): Either<Throwable, Unit>

    suspend fun searchQuery(): Either<Throwable, String>
}