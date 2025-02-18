package com.gchristov.newsfeed.multiplatform.post.testfixtures

import com.gchristov.newsfeed.multiplatform.post.data.Post

object PostCreator {
    const val PostId = "post_123"
    private const val PostDate = "2022-02-21T00:00:00Z"

    fun post(): Post = Post(
        id = PostId,
        date = PostDate,
        headline = "Post Title",
        body = "This is a sample post body",
        thumbnail = null,
    )
}