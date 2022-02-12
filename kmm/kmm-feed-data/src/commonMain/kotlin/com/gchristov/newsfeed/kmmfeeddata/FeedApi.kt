package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmcommonnetwork.ApiClient
import com.gchristov.newsfeed.kmmfeeddata.api.ApiFeedResponse
import io.ktor.client.request.*

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