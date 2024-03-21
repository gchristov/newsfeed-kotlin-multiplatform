package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

internal class PostApi(
    private val client: NetworkClient.Json,
    private val config: NetworkConfig,
) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String,
    ): HttpResponse =
        client.http.get("${config.guardianApiUrl}/$postUrl?show-fields=$postMetadataFields") {
            header("api-key", config.guardianApiKey)
        }
}