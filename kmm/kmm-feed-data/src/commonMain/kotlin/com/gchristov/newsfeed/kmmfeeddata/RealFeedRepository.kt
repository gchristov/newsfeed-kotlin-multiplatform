package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmfeeddata.model.*
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.datetime.Clock

internal class RealFeedRepository(
    private val apiService: FeedApi,
    private val sharedPreferences: Settings,
    database: FeedSqlDelightDatabase
) : FeedRepository {
    private val queries = database.feedSqlDelightDatabaseQueries

    override suspend fun feed(pageId: String?): Feed {
        val cache = cachedFeed(pageId)
        if (cache.isNotEmpty()) {
            return Feed(
                posts = cache,
                paging = PageCursor(cache.first().post.nextPageId ?: "")
            )
        }
        val feed = apiService.feed(pageId).toFeed(repository = this)
        cacheFeed(
            feed = feed,
            pageId = pageId
        )
        return feed
    }

    override suspend fun clearCache() {
        queries.transaction {
            queries.clearTable()
        }
    }

    override suspend fun post(postId: String): DecoratedPost {
        return queries.selectWithId(postId).executeAsOne().decorate(repository = this)
    }

    override fun favouriteTimestamp(postId: String): Long? {
        if (sharedPreferences.contains(postId)) {
            return sharedPreferences.getLong(postId, Clock.System.now().toEpochMilliseconds())
        }
        return null
    }

    override fun toggleFavourite(postId: String): Long? {
        favouriteTimestamp(postId)?.let {
            sharedPreferences.remove(postId)
            return null
        } ?: run {
            // Keep track of when the item was favourited
            sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            return sharedPreferences[postId]
        }
    }

    private fun cacheFeed(
        feed: Feed,
        pageId: String?
    ) {
        feed.posts.forEach { decoratedPost ->
            val post = decoratedPost.post
            queries.insert(
                uid = post.uid,
                author = post.author,
                title = post.title,
                body = post.body,
                pageId = pageId,
                nextPageId = feed.paging.next_cursor
            )
        }
    }

    private fun cachedFeed(pageId: String?): List<DecoratedPost> {
        return queries.selectWithPageId(pageId).executeAsList()
            .map { it.decorate(repository = this) }
    }
}