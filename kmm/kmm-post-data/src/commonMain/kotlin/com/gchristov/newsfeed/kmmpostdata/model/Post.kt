package com.gchristov.newsfeed.kmmpostdata.model

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.api.ApiPost
import com.gchristov.newsfeed.kmmpostdata.api.ApiPostResponse
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import kotlinx.datetime.Instant
import kotlin.math.truncate

data class DecoratedPost(
    val raw: Post,
    // Additional properties
    val date: Instant,
    val favouriteTimestamp: Long? = null, // Will be set later if post has been added to favourites
    val readingTimeMinutes: Int
)

fun ApiPost.toPost() = Post(
    id = id,
    date = webPublicationDate,
    body = fields?.body,
    headline = fields?.headline,
    thumbnail = fields?.thumbnail
)

internal fun ApiPostResponse.toPost() = response.content.toPost()

fun Post.calculateReadingTime(): Int {

    val bodyWordCount = this.body?.split(" ")?.count() ?: 0
    val headerWordCount = this.headline?.split(" ")?.count() ?: 0
    val totalWordCount = bodyWordCount + headerWordCount

    return ReadingTimeCalculator.calculateReadingTime(totalWordCount)
}
