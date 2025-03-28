package com.gchristov.newsfeed.multiplatform.feed.data

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.auth.data.AuthRepository
import com.gchristov.newsfeed.multiplatform.feed.data.db.DbFeedFilter
import com.gchristov.newsfeed.multiplatform.feed.data.db.toFeedFilter
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.FeedFilter
import com.gchristov.newsfeed.multiplatform.feed.data.model.toFeedPage
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

interface FeedRepository {
    suspend fun feedPage(
        pageId: Int,
        filter: FeedFilter,
    ): Either<Throwable, DecoratedFeedPage>

    suspend fun redecorateFeedPage(feedPage: DecoratedFeedPage): Either<Throwable, DecoratedFeedPage>

    suspend fun cachedFeedPage(): Either<Throwable, DecoratedFeedPage?>

    suspend fun clearCache(): Either<Throwable, Unit>

    suspend fun saveFeedFilter(filter: FeedFilter): Either<Throwable, Unit>

    suspend fun feedFilter(): Either<Throwable, FeedFilter>
}

internal class RealFeedRepository(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: FeedApi,
    private val postRepository: PostRepository,
    database: FeedSqlDelightDatabase,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
) : FeedRepository {
    private val queries = database.feedSqlDelightDatabaseQueries

    override suspend fun feedPage(
        pageId: Int,
        filter: FeedFilter,
    ): Either<Throwable, DecoratedFeedPage> = withContext(dispatcher) {
        either {
            val feedRsp = apiService.feed(
                pageId = pageId,
                feedQuery = filter.query
            ).bind()
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

    override suspend fun feedFilter(): Either<Throwable, FeedFilter> = withContext(dispatcher) {
        either {
            val userSession = authRepository.getUserSession().bind()
            val feedFilterPath = "feed_filter/${userSession.userId}"
            val feedFilterDocument = firestore.document(feedFilterPath).get()

            val result: Either<Throwable, FeedFilter> = if (feedFilterDocument.exists) {
                try {
                    val dbFeedFilter: DbFeedFilter = feedFilterDocument.data()
                    Either.Right(dbFeedFilter.toFeedFilter())
                } catch (e: Throwable) {
                    Either.Left(e)
                }
            } else {
                Either.Right(FeedFilter.Default)
            }
            result.bind()
        }
    }

    override suspend fun saveFeedFilter(
        filter: FeedFilter
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        either {
            val userSession = authRepository.getUserSession().bind()
            val feedFilterPath = "feed_filter/${userSession.userId}"

            try {
                firestore.document(feedFilterPath).set(filter.toFeedFilter())
                Either.Right(Unit)
            } catch (e: Throwable) {
                Either.Left(e)
            }
        }
    }
}