package com.gchristov.newsfeed.multiplatform.post.data.model

import com.gchristov.newsfeed.multiplatform.post.data.Post
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPost
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import kotlinx.datetime.Instant

data class DecoratedPost(
    val raw: Post,
    // Additional properties
    val date: Instant,
    val favouriteTimestamp: Long? = null, // Will be set later if post has been added to favourites
    val readingTimeMinutes: Int? = null // calculated at decoration time
)

fun ApiPost.toPost() = Post(
    id = id,
    date = webPublicationDate,
    body = fields?.body,
    headline = fields?.headline,
    thumbnail = fields?.thumbnail
)

internal fun ApiPostResponse.toPost() = response.content.toPost()
