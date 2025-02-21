package com.gchristov.newsfeed.multiplatform.feed.data.model

import com.gchristov.newsfeed.multiplatform.feed.data.FeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.api.ApiFeedResponse
import kotlinx.datetime.Instant

data class DecoratedFeedPage(
    val raw: FeedPage,
    // Additional properties
    val items: List<DecoratedFeedItem>
)

data class DecoratedFeedItem(
    val raw: FeedItem,
    // Additional properties
    val date: Instant,
    val favouriteTimestamp: Long?
)

internal inline fun ApiFeedResponse.toFeedPage(itemDecorator: (FeedItem) -> DecoratedFeedItem): DecoratedFeedPage {
    val page = FeedPage(
        pageId = response.currentPage.toLong(),
        pages = response.pages.toLong(),
    )
    val items = response.results.map { itemDecorator(it.toFeedItem(pageId = page.pageId.toInt())) }
    return DecoratedFeedPage(
        raw = page,
        items = items
    )
}

private fun ApiFeedResponse.ApiFeedItem.toFeedItem(pageId: Int): FeedItem = FeedItem(
    itemId = id,
    pageId = pageId.toLong(),
    apiUrl = apiUrl,
    date = webPublicationDate,
    headline = fields?.headline,
    thumbnail = fields?.thumbnail,
)
