package com.gchristov.newsfeed.kmmposttestfixtures

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import kotlinx.datetime.Instant

object PostCreator {
    private const val Date = "2022-02-21T00:00:00Z"

    fun post(favouriteTimestamp: Long? = null): DecoratedPost {
        return DecoratedPost(
            raw = Post(
                id = "post_123",
                date = Date,
                headline = "Post Title",
                body = "This is a sample post body",
                thumbnail = null,
            ),
            date = Instant.parse(Date),
            favouriteTimestamp = favouriteTimestamp,
            readingTimeMinutes = 0
        )
    }
}