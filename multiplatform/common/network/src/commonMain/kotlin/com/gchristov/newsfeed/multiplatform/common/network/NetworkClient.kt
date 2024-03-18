package com.gchristov.newsfeed.multiplatform.common.network

import com.gchristov.newsfeed.multiplatform.common.kotlin.JsonSerializer
import com.gchristov.newsfeed.multiplatform.common.kotlin.debug
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

sealed class NetworkClient {
    abstract val http: HttpClient

    data class Json(
        val log: co.touchlab.kermit.Logger,
        val jsonSerializer: JsonSerializer,
    ) : NetworkClient() {
        override val http: HttpClient = buildHttpClient(
            log = log,
            logLevel = LogLevel.ALL,
        ).config {
            install(ContentNegotiation) {
                json(jsonSerializer.json)
            }
        }
    }
}

private fun buildHttpClient(
    log: co.touchlab.kermit.Logger,
    logLevel: LogLevel,
) = HttpClient {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                log.debug(tag = "HttpClient", message = message)
            }
        }
        level = logLevel
    }
}