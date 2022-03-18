package com.gchristov.newsfeed.kmmpostdata.model

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.api.ApiPost
import com.gchristov.newsfeed.kmmpostdata.api.ApiPostResponse
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import kotlinx.coroutines.*
import kotlinx.datetime.Instant

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

internal fun Post.dispatcher() = Dispatchers.Default

suspend fun Post.calculateReadingTime(): Int {
    val body = this.body
    val headline = this.headline
    val wordCount = withContext(this.dispatcher()) {
            val bodyWordCount = body?.split(" ")?.count() ?: 0
            val headerWordCount = headline?.split(" ")?.count() ?: 0
            bodyWordCount + headerWordCount
    }

    return ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
}
