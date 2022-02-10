package com.gchristov.newsfeed.kmmfeedtestfixtures

import com.gchristov.newsfeed.kmmfeeddata.Post
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost

object PostCreator {
    fun post(favouriteTimestamp: Long? = null): DecoratedPost {
        return DecoratedPost(
            post = Post(
                uid = "post_123",
                author = "steve",
                title = "Post Title",
                body = "This is a sample post body",
                pageId = null,
                nextPageId = null
            ),
            favouriteTimestamp = favouriteTimestamp
        )
    }
}