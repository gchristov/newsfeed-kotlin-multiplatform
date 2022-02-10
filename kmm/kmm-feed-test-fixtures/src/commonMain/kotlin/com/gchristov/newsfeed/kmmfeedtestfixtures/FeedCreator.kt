package com.gchristov.newsfeed.kmmfeedtestfixtures

import com.gchristov.newsfeed.kmmfeeddata.Post
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeeddata.model.Feed
import com.gchristov.newsfeed.kmmfeeddata.model.PageCursor

object FeedCreator {
    fun singlePageFeed(): List<Feed> {
        return listOf(
            Feed(
                posts = listOf(
                    DecoratedPost(
                        post = Post(
                            uid = "post_1",
                            author = "steve",
                            title = "Post 1 Title",
                            body = "This is a sample post 1 body",
                            pageId = null,
                            nextPageId = null
                        ),
                        favouriteTimestamp = null
                    ),
                    DecoratedPost(
                        post = Post(
                            uid = "post_2",
                            author = "amy",
                            title = "Post 2 Title",
                            body = "This is a sample post 2 body that may be longer and even go on multiple lines",
                            pageId = null,
                            nextPageId = null
                        ),
                        favouriteTimestamp = 123L
                    ),
                ),
                paging = PageCursor(next_cursor = "")
            )
        )
    }

    fun multiPageFeed(): List<Feed> {
        return listOf(
            Feed(
                posts = listOf(
                    DecoratedPost(
                        post = Post(
                            uid = "post_1",
                            author = "steve",
                            title = "Post 1 Title",
                            body = "This is a sample post 1 body",
                            pageId = null,
                            nextPageId = "1"
                        ),
                        favouriteTimestamp = null
                    ),
                ),
                paging = PageCursor(next_cursor = "1")
            ),
            Feed(
                posts = listOf(
                    DecoratedPost(
                        post = Post(
                            uid = "post_2",
                            author = "amy",
                            title = "Post 2 Title",
                            body = "This is a sample post 2 body that may be longer and even go on multiple lines",
                            pageId = "1",
                            nextPageId = "2"
                        ),
                        favouriteTimestamp = 123L
                    ),
                ),
                paging = PageCursor(next_cursor = "2")
            ),
            Feed(
                posts = listOf(
                    DecoratedPost(
                        post = Post(
                            uid = "post_3",
                            author = "sarah",
                            title = "Post 3 Title",
                            body = "This is a sample post 3 body",
                            pageId = "3",
                            nextPageId = null
                        ),
                        favouriteTimestamp = 123L
                    ),
                ),
                paging = PageCursor(next_cursor = "")
            )
        )
    }
}