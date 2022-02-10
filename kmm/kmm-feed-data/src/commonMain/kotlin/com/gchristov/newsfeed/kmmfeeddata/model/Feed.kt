package com.gchristov.newsfeed.kmmfeeddata.model

import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.Post
import com.gchristov.newsfeed.kmmfeeddata.api.ApiFeedResponse
import com.gchristov.newsfeed.kmmfeeddata.api.ApiPageCursor
import com.gchristov.newsfeed.kmmfeeddata.api.ApiPost

data class Feed(
    val posts: List<DecoratedPost>,
    val paging: PageCursor
)

data class PageCursor(val next_cursor: String)

internal fun ApiFeedResponse.toFeed(repository: FeedRepository) = Feed(
    posts = posts.map { it.toPost().decorate(repository) },
    paging = paging.toPageCursor()
)

private fun ApiPost.toPost() = Post(
    uid = uid,
    author = author,
    title = title,
    body = body,
    pageId = pageId,
    nextPageId = nextPageId
)

private fun ApiPageCursor.toPageCursor() = PageCursor(next_cursor = next_cursor)
