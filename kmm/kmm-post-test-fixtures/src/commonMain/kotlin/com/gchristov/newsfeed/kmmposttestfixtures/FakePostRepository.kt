package com.gchristov.newsfeed.kmmposttestfixtures

import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmcommontest.execute
import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import kotlinx.datetime.Clock

class FakePostRepository(
    val post: DecoratedPost? = null,
    val postCache: DecoratedPost? = null,
) : PostRepository {
    var postResponse: FakeResponse = FakeResponse.CompletesNormally

    private var _cacheCleared = false
    private var _favouritePosts = mutableMapOf<String, Long>().apply {
        post?.let { post ->
            post.favouriteTimestamp?.let { put(post.raw.id, it) }
        }
    }

    override suspend fun post(postId: String, postMetadataFields: String): DecoratedPost {
        return postResponse.execute(requireNotNull(post))
    }

    override suspend fun redecoratePost(post: DecoratedPost): DecoratedPost {
        return post.copy(favouriteTimestamp = favouriteTimestamp(post.raw.id))
    }

    override suspend fun cachedPost(postId: String): DecoratedPost? {
        return postCache
    }

    override suspend fun clearCache(postId: String) {
        _cacheCleared = true
    }

    override suspend fun favouriteTimestamp(postId: String): Long? {
        return _favouritePosts[postId]
    }

    override suspend fun toggleFavourite(postId: String) {
        favouriteTimestamp(postId)?.let {
            _favouritePosts.remove(postId)
        } ?: run {
            // Keep track of when the item was favourited
            _favouritePosts[postId] = Clock.System.now().toEpochMilliseconds()
        }
    }

    override suspend fun calculateReadingTimeMinutes(post: Post): Int {
        return 1
    }

    fun assertCacheCleared() = _cacheCleared
}