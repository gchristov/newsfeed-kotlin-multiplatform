package com.gchristov.newsfeed.kmmcommontest

import kotlinx.coroutines.delay

sealed class FakeResponse {
    data class Error(val message: String? = null) : FakeResponse()
    object CompletesNormally : FakeResponse()
    object LoadsForever : FakeResponse()
}

suspend fun <T> FakeResponse.execute(value: T): T = when (this) {
    is FakeResponse.CompletesNormally -> value
    is FakeResponse.Error -> throw Exception(message)
    is FakeResponse.LoadsForever -> {
        // Artificial delay for tests
        delay(10000)
        value
    }
}