package com.gchristov.newsfeed.multiplatform.post.testfixtures

import com.gchristov.newsfeed.multiplatform.post.data.Post

object PostCreator {
    fun post(
        id: String = "post_123",
        title: String = "Post Title",
        body: String = "This is a sample post body",
        date: String = "2022-02-21T00:00:00Z",
    ): Post = Post(
        id = id,
        date = date,
        headline = title,
        body = body,
        thumbnail = null,
    )
}