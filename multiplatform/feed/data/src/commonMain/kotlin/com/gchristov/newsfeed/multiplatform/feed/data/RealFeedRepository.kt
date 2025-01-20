package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.feed.data.api.ApiFeedResponse
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.toFeedPage
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

internal class RealFeedRepository(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: FeedApi,
    private val postRepository: PostRepository,
    database: FeedSqlDelightDatabase,
    private val sharedPreferences: Settings
) : FeedRepository {
    private val queries = database.feedSqlDelightDatabaseQueries

    override suspend fun feedPage(
        pageId: Int,
        feedQuery: String,
    ): DecoratedFeedPage = withContext(dispatcher) {
        val feedResponse: ApiFeedResponse = apiService.feed(
            pageId = pageId,
            feedQuery = feedQuery
        ).body()
        val feedPage = feedResponse.toFeedPage { decorateFeedItem(feedItem = it) }
        cacheFeedPage(feedPage)
        feedPage
    }

    override suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): DecoratedFeedPage =
        withContext(dispatcher) {
            val redecoratedFeedItems = feedPage.items.map { decorateFeedItem(it.raw) }
            feedPage.copy(items = redecoratedFeedItems)
        }

    /**
     * Decorates a [feedItem]. Should be called from a background thread.
     */
    private suspend fun decorateFeedItem(feedItem: FeedItem) = DecoratedFeedItem(
        raw = feedItem,
        date = Instant.parse(feedItem.date),
        favouriteTimestamp = postRepository.favouriteTimestamp(feedItem.itemId)
    )

    override suspend fun cachedFeedPage(): DecoratedFeedPage? =
        withContext(dispatcher) {
            val selectPage = queries.selectFeedPage().executeAsList()
            if (selectPage.isEmpty()) {
                return@withContext null
            }
            // We only cache the first feed page, so using the first result is fine here
            val firstPage = selectPage.first()
            val page = FeedPage(
                pageId = firstPage.pageId,
                pages = firstPage.pages,
            )
            val feedItems = selectPage.map {
                decorateFeedItem(
                    FeedItem(
                        itemId = it.itemId,
                        pageId = it.pageId,
                        apiUrl = it.apiUrl,
                        date = it.date,
                        headline = it.headline,
                        thumbnail = it.thumbnail
                    )
                )
            }
            DecoratedFeedPage(
                raw = page,
                items = feedItems
            )
        }

    override suspend fun clearCache() =
        withContext(dispatcher) {
            queries.transaction {
                queries.clearFeedPages()
                queries.clearFeedItems()
            }
        }

    /**
     * Saves [feedPage] to local storage synchronously. Should be called from a background thread.
     */
    private fun cacheFeedPage(feedPage: DecoratedFeedPage) {
        queries.transaction {
            queries.insertFeed(
                pageId = feedPage.raw.pageId,
                pages = feedPage.raw.pages,
            )
            feedPage.items.forEach { feedItem ->
                val post = feedItem.raw
                queries.insertFeedItem(
                    itemId = post.itemId,
                    pageId = feedPage.raw.pageId,
                    apiUrl = post.apiUrl,
                    date = post.date,
                    headline = post.headline,
                    thumbnail = post.thumbnail,
                )
            }
        }
    }

    override suspend fun searchQuery(): String? = withContext(dispatcher) {
        return@withContext sharedPreferences.getStringOrNull(key = SEARCH_QUERY_PREFERENCES_KEY)
    }

    override suspend fun saveSearchQuery(searchQuery: String) = withContext(dispatcher) {
        sharedPreferences[SEARCH_QUERY_PREFERENCES_KEY] = searchQuery
    }
}

private const val SEARCH_QUERY_PREFERENCES_KEY = "searchQuery"