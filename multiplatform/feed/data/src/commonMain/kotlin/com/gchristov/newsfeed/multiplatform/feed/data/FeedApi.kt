package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

internal class FeedApi(private val client: NetworkClient.Json) {
    suspend fun feed(
        pageId: Int,
        feedQuery: String
    ): HttpResponse = client.http.get(
        "${Domain}/search?"
                + "order-by=newest"
                + "&show-fields=headline,thumbnail"
                + "&page-size=20"
                + "&page=$pageId"
                + "&q=$feedQuery"
    ) {
        // TODO: Make this secret
        header("api-key", "86cb30cc-1eb4-478f-a147-f73e02862a2e")
    }
}

// TODO: Make this secret
private const val Domain = "https://content.guardianapis.com"