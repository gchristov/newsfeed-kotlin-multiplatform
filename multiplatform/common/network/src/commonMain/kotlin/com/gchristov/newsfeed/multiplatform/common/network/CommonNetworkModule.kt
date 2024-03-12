package com.gchristov.newsfeed.multiplatform.common.network

import com.gchristov.newsfeed.multiplatform.common.di.DiModule
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import org.kodein.di.DI
import org.kodein.di.bindSingleton

object CommonNetworkModule : DiModule() {
    override fun name() = "multiplatform-network-module"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton { provideNetworkClient() }
            bindSingleton { provideApiClient() }
        }
    }

    private fun provideNetworkClient() = NetworkClient(provideHttpClient())

    private fun provideApiClient() = ApiClient(provideHttpClient().config {
        defaultRequest {
            header(BuildKonfig.API_AUTH_HEADER, BuildKonfig.API_KEY)
            url.takeFrom(URLBuilder().takeFrom(BuildKonfig.API_URL).apply {
                encodedPath += url.encodedPath
            })
        }
    })

    private fun provideHttpClient() = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
}
