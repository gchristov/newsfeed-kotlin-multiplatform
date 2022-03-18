package com.gchristov.newsfeed.kmmpostdata.util

import kotlin.math.truncate

object ReadingTimeCalculator {

    fun calculateReadingTime(totalWordCount: Int): Int {
        if (totalWordCount ==  0) {
            return 0
        }

        val minutesWithDecimals = totalWordCount / 200.toDouble()
        val fullMinutes = truncate(minutesWithDecimals).toInt()

        val decimalPart = minutesWithDecimals - fullMinutes
        val extraSeconds = (decimalPart * 0.60) * 100
        val extraMinutes = if (extraSeconds.toInt() > 40) 1 else 0

        return fullMinutes + extraMinutes
    }
}