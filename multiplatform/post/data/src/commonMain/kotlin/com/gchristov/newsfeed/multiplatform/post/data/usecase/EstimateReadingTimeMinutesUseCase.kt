package com.gchristov.newsfeed.multiplatform.post.data.usecase

import arrow.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.truncate

interface EstimateReadingTimeMinutesUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, Int>

    data class Dto(val text: String)
}

class RealEstimateReadingTimeMinutesUseCase(
    private val dispatcher: CoroutineDispatcher,
) : EstimateReadingTimeMinutesUseCase {
    override suspend fun invoke(
        dto: EstimateReadingTimeMinutesUseCase.Dto
    ): Either<Throwable, Int> = withContext(dispatcher) {
        val words = dto.text.trim()
        val wordCount = words.split("\\s+".toRegex()).size

        if (dto.text.isEmpty() || dto.text.isBlank() || wordCount <= 0) {
            return@withContext Either.Right(0)
        }

        val minutesWithDecimals = wordCount / 200.toDouble()
        val fullMinutes = truncate(minutesWithDecimals).toInt()

        val decimalPart = minutesWithDecimals - fullMinutes
        val extraSeconds = (decimalPart * 0.60) * 100
        val extraMinutes = if (extraSeconds > 30) 1 else 0
        val totalMinutes = fullMinutes + extraMinutes

        Either.Right(max(1, totalMinutes))
    }
}