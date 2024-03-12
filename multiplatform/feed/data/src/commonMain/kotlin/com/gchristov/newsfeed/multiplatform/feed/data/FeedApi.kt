package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.common.network.ApiClient
import com.gchristov.newsfeed.multiplatform.feed.data.api.ApiFeedResponse
import io.ktor.client.request.get

internal class FeedApi(private val client: ApiClient) {
    suspend fun feed(
        pageId: Int,
        feedQuery: String
    ): ApiFeedResponse = client.http.get(
        "search?"
                + "order-by=newest"
                + "&show-fields=headline,thumbnail"
                + "&page-size=20"
                + "&page=$pageId"
                + "&q=$feedQuery"
    )
}