package com.gchristov.newsfeed.multiplatform.feed.testfixtures

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.common.test.execute
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.FeedFilter
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import kotlin.test.assertEquals

class FakeFeedRepository(
    private val postRepository: PostRepository,
    val feedPages: List<DecoratedFeedPage>? = null,
    val feedPageCache: DecoratedFeedPage? = null
) : FeedRepository {
    var feedResponse: FakeResponse = FakeResponse.CompletesNormally
    var feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally
    var pageIndex = 0

    private var lastFeedFilter = FeedFilter.Default

    override suspend fun feedPage(
        pageId: Int,
        filter: FeedFilter
    ): Either<Throwable, DecoratedFeedPage> {
        val fakeResponse = if (pageIndex == 0) feedResponse else feedLoadMoreResponse
        val indexToLoad = pageIndex
        if (fakeResponse !is FakeResponse.Error) {
            // Errors should retry loading the same page so do not advance the current index
            pageIndex++
        }
        return try {
            Either.Right(fakeResponse.execute(requireNotNull(feedPages)[indexToLoad]))
        } catch (error: Throwable) {
            Either.Left(error)
        }
    }

    override suspend fun redecorateFeedPage(
        feedPage: DecoratedFeedPage
    ): Either<Throwable, DecoratedFeedPage> = either {
        feedPage.copy(items = feedPage.items.map {
            it.copy(favouriteTimestamp = postRepository.favouriteTimestamp(it.raw.itemId).bind())
        })
    }

    override suspend fun cachedFeedPage(): Either<Throwable, DecoratedFeedPage?> {
        return Either.Right(feedPageCache)
    }

    override suspend fun clearCache(): Either<Throwable, Unit> {
        return Either.Right(Unit)
    }

    override suspend fun saveFeedFilter(filter: FeedFilter): Either<Throwable, Unit> {
        lastFeedFilter = filter
        return Either.Right(Unit)
    }

    override suspend fun feedFilter(): Either<Throwable, FeedFilter> {
        return Either.Right(lastFeedFilter)
    }

    fun assertFeedFilter(filter: FeedFilter) {
        assertEquals(filter, lastFeedFilter)
    }
}