package com.gchristov.newsfeed.multiplatform.common.test

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object FakeClock : Clock {
    override fun now(): Instant = Instant.parse("2022-02-22T00:00:00Z")
}