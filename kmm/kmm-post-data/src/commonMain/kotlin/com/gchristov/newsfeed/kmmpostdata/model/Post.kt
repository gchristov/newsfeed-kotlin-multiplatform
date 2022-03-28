package com.gchristov.newsfeed.kmmpostdata.model

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.api.ApiPost
import com.gchristov.newsfeed.kmmpostdata.api.ApiPostResponse
import kotlinx.datetime.Instant

data class DecoratedPost(
    val raw: Post,
    // Additional properties
    val date: Instant,
    val favouriteTimestamp: Long? = null, // Will be set later if post has been added to favourites
    val readingTimeMinutes: Int = 1
)

fun ApiPost.toPost() = Post(
    id = id,
    date = webPublicationDate,
    body = fields?.body,
    headline = fields?.headline,
    thumbnail = fields?.thumbnail
)

internal fun ApiPostResponse.toPost() = response.content.toPost()
