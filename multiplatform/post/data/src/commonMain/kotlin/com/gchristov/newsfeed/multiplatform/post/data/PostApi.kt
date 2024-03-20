package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

internal class PostApi(private val client: NetworkClient.Json) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String,
    ): HttpResponse = client.http.get("${Domain}/$postUrl?show-fields=$postMetadataFields") {
        // TODO: Make this secret
        header("api-key", "86cb30cc-1eb4-478f-a147-f73e02862a2e")
    }
}

// TODO: Make this secret
private const val Domain = "https://content.guardianapis.com"