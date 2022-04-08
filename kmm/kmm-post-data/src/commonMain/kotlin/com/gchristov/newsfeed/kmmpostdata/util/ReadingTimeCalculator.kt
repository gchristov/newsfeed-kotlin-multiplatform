package com.gchristov.newsfeed.kmmpostdata.util

import kotlin.math.truncate

object ReadingTimeCalculator {

    fun calculateReadingTimeMinutes(totalWordCount: Int): Int {
        if (totalWordCount <= 0) {
            return 0
        }

        val minutesWithDecimals = totalWordCount / 200.toDouble()
        val fullMinutes = truncate(minutesWithDecimals).toInt()

        val decimalPart = minutesWithDecimals - fullMinutes
        val extraSeconds = (decimalPart * 0.60) * 100
        val extraMinutes = if (extraSeconds.toInt() > 30) 1 else 0
        val totalMinutes = fullMinutes + extraMinutes

        return if (totalMinutes == 0) 1 else totalMinutes
    }
}