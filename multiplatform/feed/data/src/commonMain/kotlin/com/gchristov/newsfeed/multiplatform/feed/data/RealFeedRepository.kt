package com.gchristov.newsfeed.multiplatform.feed.data

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.feed.data.api.ApiFeedResponse
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.toFeedPage
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
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
    ): Either<Throwable, DecoratedFeedPage> = withContext(dispatcher) {
        either {
            val feedRsp = apiService.feed(
                pageId = pageId,
                feedQuery = feedQuery
            ).bind().body<ApiFeedResponse>()
            val feedPage = feedRsp.toFeedPage { decorateFeedItem(feedItem = it).bind() }
            clearCache().bind()
            cacheFeedPage(feedPage)
            feedPage
        }
    }

    override suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): Either<Throwable, DecoratedFeedPage> =
        withContext(dispatcher) {
            val redecoratedFeedItems = async {
                feedPage.items.map { decorateFeedItem(it.raw) }
            }
            either {
                val items = redecoratedFeedItems.await().bindAll()
                feedPage.copy(items = items)
            }
        }

    private suspend fun decorateFeedItem(
        feedItem: FeedItem
    ): Either<Throwable, DecoratedFeedItem> = withContext(dispatcher) {
        postRepository
            .favouriteTimestamp(feedItem.itemId)
            .map { timestamp ->
                DecoratedFeedItem(
                    raw = feedItem,
                    date = Instant.parse(feedItem.date),
                    favouriteTimestamp = timestamp
                )
            }
    }

    override suspend fun cachedFeedPage(): Either<Throwable, DecoratedFeedPage?> =
        withContext(dispatcher) {
            val selectPage = queries.selectFeedPage().executeAsList()
            if (selectPage.isEmpty()) {
                return@withContext Either.Right(null)
            }
            // We only cache the first feed page, so using the first result is fine here
            val firstPage = selectPage.first()
            val page = FeedPage(
                pageId = firstPage.pageId,
                pages = firstPage.pages,
            )
            val feedItems = async {
                selectPage.map {
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
            }
            either {
                val items = feedItems.await().bindAll()
                DecoratedFeedPage(
                    raw = page,
                    items = items
                )
            }
        }

    override suspend fun clearCache(): Either<Throwable, Unit> = withContext(dispatcher) {
        queries.transaction {
            queries.clearFeedPages()
            queries.clearFeedItems()
        }
        Either.Right(Unit)
    }

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

    override suspend fun searchQuery(): Either<Throwable, String> = withContext(dispatcher) {
        val cachedQuery = sharedPreferences.getStringOrNull(key = SearchQueryKey)
        Either.Right(cachedQuery ?: DefaultSearchQuery)
    }

    override suspend fun saveSearchQuery(
        searchQuery: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        sharedPreferences[SearchQueryKey] = searchQuery
        Either.Right(Unit)
    }
}

private const val SearchQueryKey = "searchQuery"
const val DefaultSearchQuery = "brexit,fintech"