package com.gchristov.newsfeed.multiplatform.feed.testfixtures

import com.gchristov.newsfeed.multiplatform.feed.data.FeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import kotlinx.datetime.Instant

object FeedCreator {
    private const val DateThisWeek = "2022-02-21T00:00:00Z"
    private const val DateLastWeek = "2022-02-13T00:00:00Z"
    private const val DateThisMonth = "2022-02-01T00:00:00Z"
    const val DateOtherMonth = "2022-01-01T00:00:00Z"

    fun emptyFeed(): List<DecoratedFeedPage> {
        return listOf(
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 0,
                    pages = 0
                ),
                items = emptyList()
            )
        )
    }

    fun singlePageFeed(): List<DecoratedFeedPage> {
        return listOf(
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 1,
                    pages = 1
                ),
                items = listOf(
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_1",
                            pageId = 1,
                            apiUrl = "http://article.com/1",
                            date = DateThisWeek,
                            headline = "Post 1 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateThisWeek),
                        favouriteTimestamp = null
                    ),
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_2",
                            pageId = 1,
                            apiUrl = "http://article.com/2",
                            date = DateLastWeek,
                            headline = "This is a sample post 2 title that may be longer and even go on multiple lines",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateLastWeek),
                        favouriteTimestamp = 123L
                    ),
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_3",
                            pageId = 1,
                            apiUrl = "http://article.com/3",
                            date = DateThisMonth,
                            headline = "Post 3 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateThisMonth),
                        favouriteTimestamp = 123L
                    ),
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_4",
                            pageId = 1,
                            apiUrl = "http://article.com/4",
                            date = DateOtherMonth,
                            headline = "Post 4 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateOtherMonth),
                        favouriteTimestamp = null
                    ),
                )
            )
        )
    }

    fun multiPageFeed(): List<DecoratedFeedPage> {
        return listOf(
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 1,
                    pages = 4
                ),
                items = listOf(
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_1",
                            pageId = 1,
                            apiUrl = "http://article.com/1",
                            date = DateThisWeek,
                            headline = "Post 1 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateThisWeek),
                        favouriteTimestamp = null
                    ),
                )
            ),
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 2,
                    pages = 4
                ),
                items = listOf(
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_2",
                            pageId = 2,
                            apiUrl = "http://article.com/2",
                            date = DateLastWeek,
                            headline = "This is a sample post 2 title that may be longer and even go on multiple lines",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateLastWeek),
                        favouriteTimestamp = 123L
                    ),
                )
            ),
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 3,
                    pages = 4
                ),
                items = listOf(
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_3",
                            pageId = 3,
                            apiUrl = "http://article.com/3",
                            date = DateThisMonth,
                            headline = "Post 3 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateThisMonth),
                        favouriteTimestamp = 123L
                    ),
                )
            ),
            DecoratedFeedPage(
                raw = FeedPage(
                    pageId = 4,
                    pages = 4
                ),
                items = listOf(
                    DecoratedFeedItem(
                        raw = FeedItem(
                            itemId = "post_4",
                            pageId = 4,
                            apiUrl = "http://article.com/4",
                            date = DateOtherMonth,
                            headline = "Post 4 Title",
                            thumbnail = null
                        ),
                        date = Instant.parse(DateOtherMonth),
                        favouriteTimestamp = null
                    ),
                )
            ),
        )
    }
}