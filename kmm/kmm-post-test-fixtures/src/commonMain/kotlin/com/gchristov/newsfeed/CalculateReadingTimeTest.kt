package com.gchristov.newsfeed

import com.gchristov.newsfeed.kmmpostdata.util.ReadingTimeCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateReadingTimeTest {

    @Test
    fun calculatesReadingTimeWhenEmpty() {
        val totalWordCount = 0;
        val readingTimeMinutes = ReadingTimeCalculator.calculateReadingTimeMinutes(totalWordCount)
        assertEquals(0, readingTimeMinutes)
    }
}