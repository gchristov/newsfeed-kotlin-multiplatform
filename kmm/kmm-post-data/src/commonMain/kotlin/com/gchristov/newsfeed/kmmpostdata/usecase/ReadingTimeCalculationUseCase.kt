package com.gchristov.newsfeed.kmmpostdata.usecase

import com.gchristov.newsfeed.kmmpostdata.Post
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ReadingTimeCalculationUseCase(private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(post: DecoratedPost): Int =
        withContext(dispatcher) {
            val bodyWordCount = post.raw.body?.split(" ")?.count() ?: 0
            val headerWordCount = post.raw.headline?.split(" ")?.count() ?: 0
            val wordCount = bodyWordCount + headerWordCount
            ReadingTimeCalculator.calculateReadingTimeMinutes(wordCount)
        }
}