package com.gchristov.newsfeed.kmmfeedtestfixtures

import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmcommontest.execute
import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeeddata.model.Feed
import kotlinx.datetime.Clock

class FakeFeedRepository(
    val feed: List<Feed>? = null,
    val post: DecoratedPost? = null,
) : FeedRepository {
    var feedResponse: FakeResponse = FakeResponse.CompletesNormally
    var feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally
    var postResponse: FakeResponse = FakeResponse.CompletesNormally

    private var _cacheCleared = false
    private var _pageIndex = 0
    private var _favouritePosts = mutableMapOf<String, Long>().apply {
        // Build favourites map from feed
        feed?.forEach { feed ->
            feed.posts.forEach { post ->
                if (post.isFavourite()) {
                    put(post.post.uid, requireNotNull(post.favouriteTimestamp))
                }
            }
        }
        // Build favourites map from post
        post?.let {
            if (post.isFavourite()) {
                put(post.post.uid, requireNotNull(post.favouriteTimestamp))
            }
        }
    }

    override suspend fun feed(pageId: String?): Feed {
        val fakeResponse = if (_pageIndex == 0) feedResponse else feedLoadMoreResponse
        val indexToLoad = _pageIndex
        if (fakeResponse !is FakeResponse.Error) {
            // Errors should retry loading the same page so do not advance the current index
            _pageIndex++
        }
        return fakeResponse.execute(requireNotNull(feed)[indexToLoad])
    }

    override suspend fun clearCache() {
        _cacheCleared = true
    }

    override suspend fun post(postId: String): DecoratedPost {
        return postResponse.execute(post) ?: throw NullPointerException()
    }

    override fun favouriteTimestamp(postId: String): Long? {
        return _favouritePosts[postId]
    }

    override fun toggleFavourite(postId: String): Long? {
        favouriteTimestamp(postId)?.let {
            return null
        } ?: run {
            // Keep track of when the item was favourited
            _favouritePosts[postId] = Clock.System.now().toEpochMilliseconds()
            return _favouritePosts[postId]
        }
    }

    fun resetCurrentPage() {
        _pageIndex = 0
    }

    fun assertCacheCleared() = _cacheCleared
}