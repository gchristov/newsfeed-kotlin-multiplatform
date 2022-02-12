package com.gchristov.newsfeed.kmmcommonnetwork

import com.gchristov.newsfeed.kmmcommondi.DiModule
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton

object CommonNetworkModule : DiModule() {
    override fun name() = "kmm-network-module"

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
