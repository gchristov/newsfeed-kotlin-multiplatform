package com.gchristov.newsfeed.kmmcommontest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

sealed class FakeResponse {
    data class Error(val message: String? = null) : FakeResponse()
    object CompletesNormally : FakeResponse()
    object LoadsForever : FakeResponse()
}

suspend fun <T> FakeResponse.execute(value: T): T = when (this) {
    is FakeResponse.CompletesNormally -> value
    is FakeResponse.Error -> throw Exception(message)
    is FakeResponse.LoadsForever -> withContext(Dispatchers.Default) {
        // Simulate an artificial delay for tests which is run on a separate background context to
        // suspend the calling coroutine
        delay(10000)
        value
    }
}