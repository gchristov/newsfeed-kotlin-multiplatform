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

//internal fun Post.dispatcher() = Dispatchers.Default

//suspend fun Post.calculateReadingTime(dispatcher: CoroutineDispatcher): Int  {
//    return calculateReadingTimeMinutes(this, dispatcher)
//}

//suspend fun calculateReadingTimeMinutes(post: Post, dispatcher: CoroutineDispatcher): Int = withContext(dispatcher) {
//    val bodyWordCount = post.body?.split(" ")?.count() ?: 0
//    val headerWordCount = post.headline?.split(" ")?.count() ?: 0
//    val wordCount = bodyWordCount + headerWordCount
//    ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
//}

//    val body = this.body
//    val headline = this.headline
//    val wordCount = withContext(this.dispatcher()) {
//            val bodyWordCount = body?.split(" ")?.count() ?: 0
//            val headerWordCount = headline?.split(" ")?.count() ?: 0
//            bodyWordCount + headerWordCount
//    }
//
//    return ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
