package com.gchristov.newsfeed.kmmfeeddata.model

fun Feed.merge(feed: Feed) = Feed(
    posts = posts.toMutableList().apply {
        addAll(feed.posts)
    },
    paging = feed.paging
)