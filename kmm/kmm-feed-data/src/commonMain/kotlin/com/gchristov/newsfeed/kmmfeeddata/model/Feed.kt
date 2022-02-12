package com.gchristov.newsfeed.kmmfeeddata.model

import com.gchristov.newsfeed.kmmfeeddata.FeedItem
import com.gchristov.newsfeed.kmmfeeddata.FeedPage
import com.gchristov.newsfeed.kmmfeeddata.api.ApiFeedItem
import com.gchristov.newsfeed.kmmfeeddata.api.ApiFeedResponse
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
        pageId = response.currentPage,
        pages = response.pages,
    )
    val items = response.results.map { itemDecorator(it.toFeedItem(pageId = page.pageId)) }
    return DecoratedFeedPage(
        raw = page,
        items = items
    )
}

private fun ApiFeedItem.toFeedItem(pageId: Int): FeedItem = FeedItem(
    itemId = id,
    pageId = pageId,
    apiUrl = apiUrl,
    date = webPublicationDate,
    headline = fields?.headline,
    thumbnail = fields?.thumbnail,
)
