package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

internal class FeedApi(
    private val client: NetworkClient.Json,
    private val config: NetworkConfig,
) {
    suspend fun feed(
        pageId: Int,
        feedQuery: String
    ): HttpResponse = client.http.get(
        "${config.guardianApiUrl}/search?"
                + "order-by=newest"
                + "&show-fields=headline,thumbnail"
                + "&page-size=20"
                + "&page=$pageId"
                + "&q=$feedQuery"
    ) {
        header("api-key", config.guardianApiKey)
    }
}